# MiniLinked Pro – Advanced (Spring Boot Full Stack)

Features:
- Spring Boot backend with JWT authentication
- MySQL support (production) + H2 for quick testing
- File upload for profile avatar
- Posts with like system
- Frontend: HTML/CSS/JS served from Spring Boot static folder

## Run (H2 demo)
mvn spring-boot:run

## Run with MySQL
- Create database: mini_linked
- Update src/main/resources/application.properties with your MySQL URL, username, password
- mvn spring-boot:run

## Default seeded users
- priya@example.com  (password used: the hashed value corresponds to 'Password@123' if you want to seed)

