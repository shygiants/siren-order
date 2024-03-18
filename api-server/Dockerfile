FROM gradle:8.6.0-jdk17 as build

WORKDIR /app

COPY . .

RUN ./gradlew assemble

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/build/libs/sirenorder-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "sirenorder-0.0.1-SNAPSHOT.jar"]