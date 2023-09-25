FROM amazoncorretto:20
WORKDIR /app
COPY build/libs/RKSS_5PR-1.jar app.jar
EXPOSE 80
CMD ["java", "-Dspring.datasource.url=jdbc:postgresql://host.docker.internal:5432/postgres", "-jar", "app.jar"]
