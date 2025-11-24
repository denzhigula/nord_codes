package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Business Logic")
@Feature("LOGOUT Action")
@TestMethodOrder(TestOrder.class)
public class LogoutTest extends BaseTest {

    @Test
    @DisplayName("TC_21: Успешный LOGOUT после LOGIN должен возвращать статус 200")
    @Story("Логика LOGOUT действия")
    @Severity(SeverityLevel.BLOCKER)
    void successfulLogoutAfterLoginShouldReturn200() {
        String token = tokenGenerator.generateValidToken();

        // Сначала выполняем LOGIN
        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode())
                .as("LOGIN должен возвращать статус 200")
                .isEqualTo(200);

        // Затем выполняем LOGOUT
        ApiResponse logoutResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(logoutResponse.getStatusCode())
                .as("LOGOUT после LOGIN должен возвращать статус 200")
                .isEqualTo(200);

        assertThat(logoutResponse.getResult())
                .as("LOGOUT должен возвращать OK")
                .isEqualTo(TestData.RESULT_OK);
    }

    @Test
    @DisplayName("TC_22: ACTION после LOGOUT должен возвращать ошибку")
    @Story("Логика LOGOUT действия")
    @Severity(SeverityLevel.CRITICAL)
    void actionAfterLogoutShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        // LOGIN -> LOGOUT
        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        ApiResponse logoutResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(logoutResponse.getStatusCode()).isEqualTo(200);

        // ACTION после LOGOUT
        ApiResponse actionResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);
        assertThat(actionResponse.getResult())
                .as("ACTION после LOGOUT должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(actionResponse.getStatusCode())
                .as("ACTION после LOGOUT должен возвращать статус ошибки")
                .isIn(400, 403, 409);
    }

    @Test
    @DisplayName("TC_23: Повторный LOGOUT после LOGOUT должен обрабатываться")
    @Story("Логика LOGOUT действия")
    @Severity(SeverityLevel.NORMAL)
    void repeatedLogoutAfterLogout() {
        String token = tokenGenerator.generateValidToken();

        // LOGIN -> LOGOUT
        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        ApiResponse firstLogout = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(firstLogout.getStatusCode()).isEqualTo(200);

        // Повторный LOGOUT
        ApiResponse secondLogout = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(secondLogout.getResult())
                .as("Повторный LOGOUT должен обрабатываться")
                .isNotNull();
    }

    @Test
    @DisplayName("TC_24: LOGOUT без предварительного LOGIN должен обрабатываться")
    @Story("Логика LOGOUT действия")
    @Severity(SeverityLevel.NORMAL)
    void logoutWithoutLoginShouldBeProcessed() {
        String token = tokenGenerator.generateValidToken();

        // LOGOUT без LOGIN
        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);

        assertThat(response.getResult())
                .as("LOGOUT без LOGIN должен возвращать ответ")
                .isNotNull();

        assertThat(response.getStatusCode())
                .as("LOGOUT без LOGIN должен возвращать валидный статус код")
                .isBetween(200, 499);
    }

    @Test
    @DisplayName("TC_25: LOGIN после LOGOUT должен работать корректно")
    @Story("Логика LOGOUT действия")
    @Severity(SeverityLevel.NORMAL)
    void loginAfterLogoutShouldWork() {
        String token = tokenGenerator.generateValidToken();

        // Первая сессия: LOGIN -> LOGOUT
        ApiResponse firstLogin = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(firstLogin.getStatusCode()).isEqualTo(200);

        ApiResponse logout = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(logout.getStatusCode()).isEqualTo(200);

        // Вторая сессия: LOGIN после LOGOUT
        ApiResponse secondLogin = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(secondLogin.getResult())
                .as("LOGIN после LOGOUT должен обрабатываться")
                .isNotNull();

        assertThat(secondLogin.getStatusCode())
                .as("LOGIN после LOGOUT должен возвращать валидный статус")
                .isBetween(200, 499);
    }
}