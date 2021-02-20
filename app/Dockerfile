FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

# copy the packaged jar file into our docker image
COPY target/highload2021-1.0-SNAPSHOT.jar /highload2021.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/highload2021.jar"]