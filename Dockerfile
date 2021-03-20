FROM maven:3.6.3-openjdk-15
USER 1000:1000
WORKDIR /app
COPY SearchService/target/SearchService.jar .
CMD ["java", "-jar", "/app/SearchService.jar" ]