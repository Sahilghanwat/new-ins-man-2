package com.example.institute.exception;
import org.springframework.dao.DataIntegrityViolationException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.time.Instant; import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<Map<String,Object>> handleStatus(ResponseStatusException ex){return response(ex.getStatusCode(),ex.getReason()==null?"Request failed.":ex.getReason());}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex){String msg=ex.getBindingResult().getFieldErrors().stream().map(e->e.getField()+": "+e.getDefaultMessage()).findFirst().orElse("Please check the submitted fields.");return response(HttpStatus.BAD_REQUEST,msg);}
 @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<Map<String,Object>> handleData(DataIntegrityViolationException ex){return response(HttpStatus.CONFLICT,"A unique field already exists or this record is still in use.");}
 @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,Object>> handleOther(Exception ex){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error. Check the backend console for details.");}
 private ResponseEntity<Map<String,Object>> response(HttpStatusCode status,String message){Map<String,Object> body=new LinkedHashMap<>();body.put("timestamp",Instant.now().toString());body.put("status",status.value());body.put("message",message);return ResponseEntity.status(status).body(body);}
}
