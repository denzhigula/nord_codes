package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Validation")
@Feature("Validation of Input Parameters")
@TestMethodOrder(TestOrder.class)
public class ValidationTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(ints = {31, 33})
    @DisplayName("TC_05: Запрос с токеном неверной длины должен возвращать ошибку 400")
    @Story("Валидация токена")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithInvalidTokenLengthShouldReturnError(int length) {
        String invalidToken = tokenGenerator.generateInvalidToken(length);

        ApiResponse response = apiClient.sendRequest(invalidToken, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Токен длиной %d символов должен возвращать ERROR", length)
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Невалидный токен должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_06: Запрос с токеном, содержащим недопустимые символы, должен возвращать ошибку 400")
    @Story("Валидация токена")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithInvalidTokenCharactersShouldReturnError() {
        String invalidToken = tokenGenerator.generateTokenWithInvalidChars();

        ApiResponse response = apiClient.sendRequest(invalidToken, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Токен с недопустимыми символами должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Токен с недопустимыми символами должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_07: Запрос без токена должен возвращать ошибку 400")
    @Story("Валидация токена")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithoutTokenShouldReturnError() {
        ApiResponse response = apiClient.sendRequest("", TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос без токена должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос без токена должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_08: Запрос с null токеном должен возвращать ошибку 400")
    @Story("Валидация токена")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithNullTokenShouldReturnError() {
        ApiResponse response = apiClient.sendRequest(null, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос с null токеном должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос с null токеном должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_09: Запрос с неверным action должен возвращать ошибку 400")
    @Story("Валидация action")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithInvalidActionShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, TestData.INVALID_ACTION);

        assertThat(response.getResult())
                .as("Запрос с неверным action должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Невалидный action должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_10: Запрос без action должен возвращать ошибку 400")
    @Story("Валидация action")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithoutActionShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, "");

        assertThat(response.getResult())
                .as("Запрос без action должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос без action должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_11: Запрос с null action должен возвращать ошибку 400")
    @Story("Валидация action")
    @Severity(SeverityLevel.CRITICAL)
    void requestWithNullActionShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, null);

        assertThat(response.getResult())
                .as("Запрос с null action должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("Запрос с null action должен возвращать статус 400")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("TC_12: Запрос с валидным токеном 32 символа должен возвращать статус 200")
    @Story("Валидация токена")
    @Severity(SeverityLevel.BLOCKER)
    void requestWithValid32CharTokenShouldReturn200() {
        String validToken = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(validToken, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("Запрос с валидным токеном должен возвращать ответ")
                .isNotNull();

        assertThat(response.getStatusCode())
                .as("Валидный запрос должен возвращать статус 200")
                .isEqualTo(200);

        assertThat(validToken)
                .as("Токен должен быть длиной 32 символа")
                .hasSize(32);
    }
}