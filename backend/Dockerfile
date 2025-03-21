# Используем официальный образ JDK (указывайте версию, которая вам нужна)
FROM openjdk:21

# Копируем собранный jar-файл в образ
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Открываем порт, на котором работает приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
