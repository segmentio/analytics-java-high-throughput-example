FROM java:8
WORKDIR /
ADD target/test10rps-1.0-SNAPSHOT-jar-with-dependencies.jar test10rps-1.0-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 8080
CMD ["java", "-jar", "test10rps-1.0-SNAPSHOT-jar-with-dependencies.jar"]
