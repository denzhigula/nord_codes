package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Authentication")
@Feature("X-Api-Key Validation")
@TestMethodOrder(TestOrder.class)
public class AuthenticationTest extends BaseTest {

    @Test
    @DisplayName("TC_01: Запрос без X-Api-Key должен возвращать ошибку 401")
    @Story("Проверка аутентификации")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithoutApiKeyShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequestWithoutApiKey(token, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос без API ключа должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос без API ключа должен возвращать статус 401 или 403")
                .isIn(401, 403);
    }

    @Test
    @DisplayName("TC_02: Запрос с неверным X-Api-Key должен возвращать ошибку 401")
    @Story("Проверка аутентификации")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithInvalidApiKeyShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequestWithCustomApiKey(
                token, TestData.VALID_ACTION_LOGIN, "invalid_key_123");

        assertThat(response.getResult())
                .as("Запрос с неверным API ключом должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос с неверным API ключом должен возвращать статус 401 или 403")
                .isIn(401, 403);
    }

    @Test
    @DisplayName("TC_03: Запрос с пустым X-Api-Key должен возвращать ошибку 401")
    @Story("Проверка аутентификации")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithEmptyApiKeyShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequestWithCustomApiKey(
                token, TestData.VALID_ACTION_LOGIN, "");

        assertThat(response.getResult())
                .as("Запрос с пустым API ключом должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос с пустым API ключом должен возвращать статус 401 или 403")
                .isIn(401, 403);
    }

    @Test
    @DisplayName("TC_04: Запрос с правильным X-Api-Key должен возвращать статус 200")
    @Story("Проверка аутентификации")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithValidApiKeyShouldReturn200() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос с валидным API ключом должен возвращать ответ")
                .isNotNull();

        assertThat(response.getStatusCode())
                .as("Запрос с валидным API ключом должен возвращать статус 200")
                .isEqualTo(200);
    }
}