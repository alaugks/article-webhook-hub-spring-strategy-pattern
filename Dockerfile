FROM maven:3-eclipse-temurin-21@sha256:9b4877723dadf350b452dd97d9a6401e7b56f98fa9dfe420c32ad909989c7e4c AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre@sha256:ccbad9e7cb84c27092b12c40a4143e5733e050db9c3784d84f632dcf423fb2ef
WORKDIR /app
COPY --from=build /build/target/app.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
