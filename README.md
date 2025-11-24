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

# Запуск с генерацией Allure отчета
mvn test allure:serve

# Запуск конкретного тестового класса
mvn test -Dtest=AuthenticationTest