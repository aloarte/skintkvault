FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
RUN mkdir /tmp/keys/
ADD firebase/skintker-firebase-adminsdk-9ed86-e8cbebaddb.json /tmp/keys/

ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]