FROM maven:alpine

WORKDIR /app

ADD . /app

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]