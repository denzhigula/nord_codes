# API Test Framework

Фреймворк для тестирования API на Java 17 с использованием JUnit 5, WireMock и Allure.

## Технологии
- Java 17
- JUnit 5
- WireMock
- RestAssured
- Allure Framework
- AssertJ
- Maven

## Запуск тестов

```bash
# Запуск всех тестов
mvn test

# Запуск Allure отчета
mvn allure:serve

# Запуск конкретного тестового класса
mvn test -Dtest=AuthenticationTest

#  Известные баги:
 - паттерн из документации [ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789] не работает
   как workaround нужно использовать [ABCDEF0123456789]
