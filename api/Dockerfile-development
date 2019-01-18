# Use this dockerfile for develpment and testing of the backend api
# To build: docker build -f Dockerfile-development .
# To run: docker run --env-file .env -p 8080:8080 <image_id from previous step>
# Open the browser and point it to: http://locahost:8080

# Use the ubuntu:bionic in order to run the tests
FROM ubuntu:bionic AS build

# Update packages
RUN apt-get update 
RUN apt-get install -y openjdk-8-jdk maven

# Set the correct Java version
RUN update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

# Copy POM and source into the /tmp for building.
COPY pom.xml /tmp/
COPY src /tmp/src/

# Set Working Directory
WORKDIR /tmp/
RUN mvn test package

# Start with a base image containing Java runtime
FROM openjdk:8-jdk

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Copy package from build container into the final container
COPY --from=build /tmp/target/spring-dal-0.0.1-SNAPSHOT.jar spring-dal.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/spring-dal.jar"]