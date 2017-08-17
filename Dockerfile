FROM maven:alpine

WORKDIR /app

ADD src /app
ADD pom.xml /app



#EXPOSE 8080

CMD ["mvn", "spring-boot:run"]