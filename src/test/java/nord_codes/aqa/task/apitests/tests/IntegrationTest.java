package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Integration Tests")
@Feature("End-to-End Scenarios")
@TestMethodOrder(TestOrder.class)
public class IntegrationTest extends BaseTest {

    @Test
    @DisplayName("TC_18: Полный сценарий LOGIN -> ACTION -> LOGOUT должен возвращать статус 200")
    @Story("Полный жизненный цикл")
    @Severity(SeverityLevel.CRITICAL)
    void fullScenarioShouldReturn200() {
        String token = tokenGenerator.generateValidToken();

        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        System.out.println("LOGIN - Status: " + loginResponse.getStatusCode() + ", Result: " + loginResponse.getResult());
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        ApiResponse actionResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);
        System.out.println("ACTION - Status: " + actionResponse.getStatusCode() + ", Result: " + actionResponse.getResult());
        assertThat(actionResponse.getStatusCode()).isEqualTo(200);

        ApiResponse logoutResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);
        System.out.println("LOGOUT - Status: " + logoutResponse.getStatusCode() + ", Result: " + logoutResponse.getResult());
        assertThat(logoutResponse.getStatusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("TC_19: Множественные токены должны работать независимо")
    @Story("Мульти-токен сценарий")
    @Severity(SeverityLevel.NORMAL)
    void multipleTokensShouldWorkIndependently() {
        String token1 = tokenGenerator.generateValidToken();
        String token2 = tokenGenerator.generateValidToken();

        ApiResponse login1 = apiClient.sendRequest(token1, TestData.VALID_ACTION_LOGIN);
        ApiResponse action1 = apiClient.sendRequest(token1, TestData.VALID_ACTION_ACTION);

        ApiResponse login2 = apiClient.sendRequest(token2, TestData.VALID_ACTION_LOGIN);
        ApiResponse action2 = apiClient.sendRequest(token2, TestData.VALID_ACTION_ACTION);

        assertThat(login1.getResult()).isNotNull();
        assertThat(action1.getResult()).isNotNull();
        assertThat(login2.getResult()).isNotNull();
        assertThat(action2.getResult()).isNotNull();
    }

    @Test
    @DisplayName("TC_20: Проверка структуры ответа с статус кодом")
    @Story("Структура ответов")
    @Severity(SeverityLevel.NORMAL)
    void checkApiResponseStructureWithStatusCode() {
        String token = tokenGenerator.generateValidToken();

        Response rawResponse = apiClient.sendRequestRaw(token, TestData.VALID_ACTION_LOGIN);
        ApiResponse response = rawResponse.as(ApiResponse.class);
        response.setStatusCode(rawResponse.getStatusCode());

        assertThat(rawResponse.getStatusCode()).isBetween(200, 599);
        assertThat(rawResponse.getContentType()).contains("application/json");
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getStatusCode()).isNotNull().isBetween(200, 599);

        System.out.println("Full response: " + response);
    }
}