package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Business Logic")
@Feature("Business Logic Scenarios")
@TestMethodOrder(TestOrder.class)
public class BusinessLogicTest extends BaseTest {

    @Test
    @DisplayName("TC_13: Успешная последовательность LOGIN -> ACTION должна возвращать статус 200")
    @Story("Позитивные сценарии")
    @Severity(SeverityLevel.BLOCKER)
    void successfulSequenceShouldReturn200() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode())
                .as("Успешный LOGIN должен возвращать статус 200")
                .isEqualTo(200);

        ApiResponse actionResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);
        assertThat(actionResponse.getStatusCode())
                .as("Успешный ACTION должен возвращать статус 200")
                .isEqualTo(200);
    }

    @Test
    @DisplayName("TC_14: Последовательность LOGIN -> LOGOUT должна возвращать статус 200")
    @Story("Позитивные сценарии")
    @Severity(SeverityLevel.BLOCKER)
    void loginThenLogoutSequenceShouldReturn200() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        ApiResponse logoutResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        assertThat(logoutResponse.getStatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("TC_15: ACTION без предварительного LOGIN должен возвращать ошибку 400")
    @Story("Негативные сценарии")
    @Severity(SeverityLevel.CRITICAL)
    void actionWithoutLoginShouldReturnError() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);

        assertThat(response.getResult())
                .as("ACTION без предварительного LOGIN должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        assertThat(response.getStatusCode())
                .as("ACTION без LOGIN должен возвращать статус 400")
                .isIn(400, 403, 409);
    }

    @Test
    @DisplayName("TC_16: Повторный LOGIN с тем же токеном должен обрабатываться")
    @Story("Граничные случаи")
    @Severity(SeverityLevel.NORMAL)
    void repeatedLoginWithSameToken() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse firstLogin = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        ApiResponse secondLogin = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        assertThat(firstLogin.getResult()).isNotNull();
        assertThat(secondLogin.getResult()).isNotNull();
    }

    @Test
    @DisplayName("TC_17: LOGOUT без предварительного LOGIN должен обрабатываться")
    @Story("Граничные случаи")
    @Severity(SeverityLevel.NORMAL)
    void logoutWithoutLogin() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);

        assertThat(response.getResult()).isNotNull();
    }
}