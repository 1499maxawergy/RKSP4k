# Build
FROM gradle:latest AS TEMP_BUILD
ENV DIR=/app
WORKDIR $DIR

COPY config-service/. $DIR
ADD . .
USER root
RUN gradle bootJar
RUN echo "Build Completed!"

# Run
FROM amazoncorretto:19
LABEL "authors"="Milko IKBO-16-20"
ENV DIR=/app
ENV JAR_NAME=config-service-1.0.0.jar
ENV DB_PORT=5432
WORKDIR $DIR
COPY --from=TEMP_BUILD $DIR/build/libs/$JAR_NAME .

EXPOSE 80
ONBUILD CMD echo "Сборка и запуск были произведены. Автор: Милько М."
ENTRYPOINT java -jar $JAR_NAME