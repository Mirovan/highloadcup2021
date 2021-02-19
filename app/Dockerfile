FROM openjdk:8-jre-alpine3.9

# copy the packaged jar file into our docker image
COPY target/highload2021-1.0-SNAPSHOT.jar /highload2021.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/demo.jar"]