FROM maven:3.9
WORKDIR /app
COPY . .
RUN mvn package -Dmaven.test.skip
WORKDIR /app/target
COPY .env .
ENTRYPOINT ["java", "-jar", "echoJavaTelegramBot-1.0-jar-with-dependencies.jar"]