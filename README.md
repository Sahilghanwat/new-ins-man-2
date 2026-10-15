# Institute Management System — Spring Boot Backend

Beginner-friendly REST backend for the Angular frontend. Uses Spring Boot 4.0.2, Java 17, Spring Data JPA, MySQL and server-side `HttpSession` (no JWT and no Spring Security filter chain). BCrypt password hashing comes from the small `spring-security-crypto` library; authorization is handled by the project’s `SessionService` role checks.

## Requirements
- JDK 17
- Maven 3.9+ (or use the Maven integration in STS)
- MySQL Server running locally
- Eclipse/STS with Spring Tools (or Maven project import support)

## 1. Create the database
Run in MySQL Workbench or DBeaver:

```sql
CREATE DATABASE institute_management;
```

The application uses `root` with an empty password by default. Set environment variables if your local MySQL credentials differ:

```bash
export DB_USERNAME=root
export DB_PASSWORD='your_mysql_password'
```

Windows PowerShell equivalents: `$env:DB_USERNAME="root"` and `$env:DB_PASSWORD="your_mysql_password"`.

## 2. Run in Eclipse/STS
1. Extract the ZIP.
2. Select **File → Import → Maven → Existing Maven Projects**.
3. Select the extracted project folder and finish.
4. Set project JRE to JDK 17.
5. Edit `src/main/resources/application.yml` or set `DB_USERNAME` and `DB_PASSWORD`.
6. Run `InstituteManagementApplication.java` as a Java application.
7. Backend base URL: `http://localhost:8080`; Swagger UI: `http://localhost:8080/swagger-ui.html`.

CLI alternative: `mvn spring-boot:run`.

## 3. Admin demo login
- Email: `admin@institute.local`
- Password: `Admin@12345`

This is a local demo credential. Override it before use outside a local learning environment:

```bash
export ADMIN_EMAIL=admin@example.com
export ADMIN_PASSWORD='replace-with-a-strong-password'
```

## 4. Run the Angular frontend
Run the frontend separately on `http://localhost:4200` (recommended). Backend CORS also permits `http://127.0.0.1:4200`. Use `localhost` consistently for frontend and backend so the browser sends the session cookie reliably. Browser requests must include credentials for the session cookie.

**Important:** Keep only one `PasswordEncoder` bean. This project defines it in `config/AppConfig.java`. If you previously created a separate `PasswordConfig.java` following troubleshooting instructions, delete that extra file to avoid a duplicate `passwordEncoder` bean.

## API overview
- `POST /api/students/register`
- `POST /api/trainers/register`
- `POST /api/auth/student/login`
- `POST /api/auth/trainer/login`
- `POST /api/auth/admin/login`
- `POST /api/auth/logout`
- `GET /api/auth/session`
- `GET /api/students/me`
- `GET /api/trainers/me`
- `GET /api/trainers/my-students`
- Admin-only: `GET /api/students`, `GET /api/trainers`, `PUT/DELETE /api/students/{id}`, `PUT/DELETE /api/trainers/{id}`

Registration JSON examples:

Student: `{"name":"Asha Patil","city":"Pune","email":"asha@example.com","password":"Student@123","courseName":"Java","trainerId":1}`

Trainer: `{"name":"Ravi Shah","className":"Java Morning Batch","courseName":"Java","experience":3,"email":"ravi@example.com","employeeNumber":"EMP1001","password":"Trainer@123"}`

## Notes and limitations
- Passwords are stored as BCrypt hashes. Password fields are never returned in API response DTOs.
- Roles are assigned by the backend; registration cannot select an admin role.
- Session cookies are HTTP-only and browser requests must use credentials.
- The first trainer must be registered before students can choose a trainer. The registration page loads the available trainers.
- A trainer with linked students cannot be deleted until those students are reassigned or deleted.
- This is a learning app, not a production-ready security configuration. Use HTTPS, stronger session cookie settings, CSRF protection, secret management and proper account administration before production.


## Elasticsearch student search (added)

Elasticsearch is used only for student search. MySQL remains the source of truth for registration, login, update, and delete. The existing `students` index is reused at `http://localhost:9201` by default; override it with `ELASTICSEARCH_URI` if needed.

- `GET /api/students/search?q=java` — search by name, email, course, city, or trainer name (admin session required).
- `POST /api/students/search/reindex` — rebuild the index from MySQL after logging in as admin (admin session required).

New/updated students are indexed after their MySQL save; deleted students are removed from the index. If Elasticsearch is temporarily unavailable during a save/delete, the MySQL operation remains successful and a warning is written to the backend log. Reindex after Elasticsearch returns.

For local setup, start the `es-learning9` Docker container and confirm `http://localhost:9201/_cluster/health` responds before testing search.
