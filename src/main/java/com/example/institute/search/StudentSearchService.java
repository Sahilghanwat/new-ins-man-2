package com.example.institute.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.institute.dto.StudentView;
import com.example.institute.entity.Student;
import com.example.institute.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentSearchService {
    private static final Logger log = LoggerFactory.getLogger(StudentSearchService.class);
    private static final String INDEX = "students";

    private final ElasticsearchClient client;
    private final StudentRepository students;

    public StudentSearchService(ElasticsearchClient client, StudentRepository students) {
        this.client = client;
        this.students = students;
    }

    /** Indexing is best-effort so an Elasticsearch outage does not break MySQL CRUD. */
    public void index(Student student) {
        try {
            client.index(request -> request.index(INDEX)
                    .id(student.getId().toString())
                    .document(toDocument(student)));
        } catch (Exception ex) {
            log.warn("Could not index student {} in Elasticsearch. MySQL operation remains successful.", student.getId(), ex);
        }
    }

    public void delete(Long studentId) {
        try {
            client.delete(request -> request.index(INDEX).id(studentId.toString()));
        } catch (Exception ex) {
            log.warn("Could not remove student {} from Elasticsearch.", studentId, ex);
        }
    }

    public List<StudentView> search(String text) {
        String query = text == null ? "" : text.trim();
        if (query.isEmpty()) {
            return List.of();
        }
        try {
            SearchResponse<StudentSearchDocument> response = client.search(request -> request
                            .index(INDEX)
                            .size(100)
                            .query(q -> q.multiMatch(m -> m
                                    .query(query)
                                    .fields("name", "email", "course", "city", "trainerName"))),
                    StudentSearchDocument.class);
            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .filter(doc -> doc != null)
                    .map(StudentSearchService::toView)
                    .toList();
        } catch (IOException ex) {
            throw new IllegalStateException("Elasticsearch search failed. Check that Elasticsearch is running at the configured URL.", ex);
        }
    }

    /** Rebuild the search index from MySQL, the source of truth. */
    public int reindexAll() {
        List<Student> all = students.findAll();
        try {
            client.deleteByQuery(request -> request.index(INDEX)
                    .query(query -> query.matchAll(matchAll -> matchAll)));
        } catch (Exception ex) {
            throw new IllegalStateException("Could not clear the existing student search index.", ex);
        }
        for (Student student : all) {
            try {
                client.index(request -> request.index(INDEX)
                        .id(student.getId().toString())
                        .document(toDocument(student)));
            } catch (Exception ex) {
                throw new IllegalStateException("Could not index student ID " + student.getId(), ex);
            }
        }
        return all.size();
    }

    private static StudentSearchDocument toDocument(Student student) {
        StudentSearchDocument doc = new StudentSearchDocument();
        doc.setStudentId(student.getId());
        doc.setName(student.getName());
        doc.setCity(student.getCity());
        doc.setEmail(student.getEmail());
        doc.setCourse(student.getCourseName());
        doc.setTrainerId(student.getTrainer() == null ? null : student.getTrainer().getId());
        doc.setTrainerName(student.getTrainer() == null ? null : student.getTrainer().getName());
        doc.setRole(student.getRole());
        return doc;
    }

    private static StudentView toView(StudentSearchDocument doc) {
        return new StudentView(doc.getStudentId(), doc.getName(), doc.getCity(), doc.getEmail(),
                doc.getCourse(), doc.getTrainerId(), doc.getTrainerName(), doc.getRole());
    }
}
