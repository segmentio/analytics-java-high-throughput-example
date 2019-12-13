build-run-container: container
	docker run segetsy:latest

container: clean compile
	docker build -t segetsy:latest .

run-local: clean compile
	java -jar target/test10rps-1.0-SNAPSHOT-jar-with-dependencies.jar

clean:
	mvn clean

compile: clean
	mvn compile assembly:single

.PHONY: run