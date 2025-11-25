package nord_codes.aqa.task.apitests.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static nord_codes.aqa.task.apitests.utils.TestData.*;
import static org.assertj.core.api.Assertions.*;

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
                .isIn(STATUS_UNAUTHORIZED, STATUS_FORBIDDEN);
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
                .isIn(STATUS_UNAUTHORIZED, STATUS_FORBIDDEN);
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
                .isIn(STATUS_UNAUTHORIZED, STATUS_FORBIDDEN);
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
                .isEqualTo(STATUS_OK);
    }

    @Test
    @DisplayName("TC_35: Запрос с правильным X-Api-Key и паттерном из документации должен возвращать статус 200")
    @Story("Проверка аутентификации")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithValidApiKeyAndPatternShouldReturn200() {
        String token = tokenGenerator.generateValidToken(false);

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос с валидным API ключом должен возвращать ответ")
                .isNotNull();

        assertThat(response.getStatusCode())
                .as("Запрос с валидным API ключом должен возвращать статус 200")
                .isEqualTo(STATUS_OK);
    }

}