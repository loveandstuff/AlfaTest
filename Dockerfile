FROM openjdk:8-alpine

# copy target/find-links.jar /usr/local/runme/app.jar
COPY ./AlfaTest-1.0.jar /demo.jar

EXPOSE 8080

# java -jar /usr/local/runme/app.jar
CMD ["java", "-jar", "./demo.jar"]