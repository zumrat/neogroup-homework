#
# Build stage
#
FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY src /home/app/validator/src
COPY pom.xml /home/app/validator
RUN mvn -f /home/app/validator/pom.xml clean package
EXPOSE 8088
ENTRYPOINT ["java","-jar","/home/app/validator/target/phone-validator.jar"]


