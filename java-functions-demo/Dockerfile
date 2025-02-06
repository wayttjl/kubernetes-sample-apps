FROM openjdk:21-slim as BUILD

RUN apt-get update
RUN apt-get install maven=3.8.7-1 -y

COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn --batch-mode -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:21-slim

WORKDIR /workspace

EXPOSE 8080
COPY --from=BUILD /usr/src/app/target/*.jar /workspace/app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8","-jar","/workspace/app.jar"]