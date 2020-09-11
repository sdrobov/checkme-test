# build
FROM openjdk:8-jdk-alpine

RUN mkdir /build

COPY ./gradle            /build/gradle
COPY ./resources         /build/resources
COPY ./src               /build/src
COPY ./test              /build/test
COPY ./build.gradle      /build/build.gradle
COPY ./gradle.properties /build/gradle.properties
COPY ./gradlew           /build/gradlew
COPY ./settings.gradle   /build/settings.gradle

WORKDIR /build

RUN ./gradlew build

# app
FROM openjdk:8-jre-alpine

ENV APPLICATION_USER ktor
RUN adduser --disabled-password --gecos '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY --from=0 /build/build/libs/checkme-test-0.0.1.jar /app/checkme-test.jar
WORKDIR /app

EXPOSE 8080

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "checkme-test.jar"]