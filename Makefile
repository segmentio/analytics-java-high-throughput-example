build-run-container: container
	docker run segexample:latest

container: clean compile
	docker build -t segexample:latest .

run-local: clean compile
	java -jar target/test10rps-1.0-SNAPSHOT-jar-with-dependencies.jar

clean:
	mvn clean

compile: clean
	mvn compile assembly:single

.PHONY: build-run-container
