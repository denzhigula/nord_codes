package nord_codes.aqa.task.apitests.tests;

import nord_codes.aqa.task.apitests.models.ApiResponse;
import nord_codes.aqa.task.apitests.utils.TestData;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("WireMock Integration")
@Feature("External Services Mocking")
@TestMethodOrder(TestOrder.class)
public class WireMockTest extends BaseTest {

    @Test
    @DisplayName("TC_26: LOGIN при успешном ответе внешнего сервиса /auth")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithSuccessfulExternalAuth() {
        String token = tokenGenerator.generateValidToken();

        // Настраиваем успешный ответ от внешнего сервиса
        setupExternalServiceSuccess("/auth");

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getStatusCode())
                .as("LOGIN при успешном внешнем сервисе должен возвращать 200")
                .isEqualTo(200);

        verifyExternalServiceCalled("/auth", token);
    }

    @Test
    @DisplayName("TC_27: LOGIN при ошибке внешнего сервиса /auth")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithFailedExternalAuth() {
        String token = tokenGenerator.generateValidToken();

        // Настраиваем ошибку от внешнего сервиса
        setupExternalServiceFailure("/auth", 401);

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        assertThat(response.getResult())
                .as("LOGIN при ошибке внешнего сервиса должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        verifyExternalServiceCalled("/auth", token);
    }

    @Test
    @DisplayName("TC_28: ACTION при успешном ответе внешнего сервиса /doAction")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.CRITICAL)
    void actionWithSuccessfulExternalService() {
        String token = tokenGenerator.generateValidToken();

        // Сначала LOGIN
        setupExternalServiceSuccess("/auth");
        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        // Затем ACTION с успешным внешним сервисом
        setupExternalServiceSuccess("/doAction");
        ApiResponse actionResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);

        assertThat(actionResponse.getStatusCode())
                .as("ACTION при успешном внешнем сервисе должен возвращать 200")
                .isEqualTo(200);

        verifyExternalServiceCalled("/auth", token);
        verifyExternalServiceCalled("/doAction", token);
    }

    @Test
    @DisplayName("TC_29: ACTION при ошибке внешнего сервиса /doAction")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.CRITICAL)
    void actionWithFailedExternalService() {
        String token = tokenGenerator.generateValidToken();

        // Сначала LOGIN
        setupExternalServiceSuccess("/auth");
        ApiResponse loginResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        assertThat(loginResponse.getStatusCode()).isEqualTo(200);

        // Затем ACTION с ошибкой внешнего сервиса
        setupExternalServiceFailure("/doAction", 500);
        ApiResponse actionResponse = apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);

        assertThat(actionResponse.getResult())
                .as("ACTION при ошибке внешнего сервиса должен возвращать ERROR")
                .isEqualTo(TestData.RESULT_ERROR);

        verifyExternalServiceCalled("/auth", token);
        verifyExternalServiceCalled("/doAction", token);
    }

    @Test
    @DisplayName("TC_30: Проверка вызова внешних сервисов с правильными параметрами")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.NORMAL)
    void verifyExternalServiceParameters() {
        String token = tokenGenerator.generateValidToken();

        setupExternalServiceSuccess("/auth");
        setupExternalServiceSuccess("/doAction");

        // Выполняем последовательность действий
        apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);
        apiClient.sendRequest(token, TestData.VALID_ACTION_ACTION);
        apiClient.sendRequest(token, TestData.VALID_ACTION_LOGOUT);

        // Проверяем что внешние сервисы вызывались с правильным токеном
        verify(postRequestedFor(urlEqualTo("/auth"))
                .withRequestBody(containing("token=" + token)));

        verify(postRequestedFor(urlEqualTo("/doAction"))
                .withRequestBody(containing("token=" + token)));

        // Проверяем количество вызовов
        verifyExternalServiceCalledTimes("/auth", 1);
        verifyExternalServiceCalledTimes("/doAction", 1);
    }

    @Test
    @DisplayName("TC_31: Внешние сервисы не вызываются при невалидных запросах")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.NORMAL)
    void externalServicesNotCalledForInvalidRequests() {
        String invalidToken = "invalid_token";

        // Невалидный запрос без API ключа
        apiClient.sendRequestWithoutApiKey(invalidToken, TestData.VALID_ACTION_LOGIN);

        // Невалидный токен
        apiClient.sendRequest(invalidToken, TestData.VALID_ACTION_LOGIN);

        // Невалидный action
        apiClient.sendRequest(tokenGenerator.generateValidToken(), "INVALID_ACTION");

        // Проверяем что внешние сервисы не вызывались
        verifyExternalServiceNotCalled("/auth");
        verifyExternalServiceNotCalled("/doAction");
    }

    @Test
    @DisplayName("TC_32: Мокирование таймаута внешнего сервиса")
    @Story("Мокирование внешних сервисов")
    @Severity(SeverityLevel.MINOR)
    void mockExternalServiceTimeout() {
        String token = tokenGenerator.generateValidToken();

        // Настраиваем таймаут для внешнего сервиса
        setupExternalServiceTimeout("/auth");

        ApiResponse response = apiClient.sendRequest(token, TestData.VALID_ACTION_LOGIN);

        // Приложение должно обработать таймаут
        assertThat(response.getResult()).isNotNull();
        verifyExternalServiceCalled("/auth", token);
    }

    @Test
    @DisplayName("TC_33: Проверка работы WireMock Extension")
    @Story("WireMock Configuration")
    @Severity(SeverityLevel.NORMAL)
    void verifyWireMockExtension() {
        assertThat(isWireMockRunning())
                .as("WireMock extension should be running")
                .isTrue();

        assertThat(wireMockServer)
                .as("WireMock server should be initialized")
                .isNotNull();

        assertThat(wireMockServer.isRunning())
                .as("WireMock server should be running")
                .isTrue();

        System.out.println("WireMock extension is working correctly on port: " + wireMockServer.port());
    }
}