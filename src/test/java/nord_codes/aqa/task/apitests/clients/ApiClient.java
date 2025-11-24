package nord_codes.aqa.task.apitests.clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import nord_codes.aqa.task.apitests.config.TestConfig;
import nord_codes.aqa.task.apitests.models.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiClient {

    static {
        RestAssured.config = RestAssuredConfig.config()
                .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());

        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public ApiResponse sendRequest(String token, String action) {
        return executeRequest(token, action, TestConfig.API_KEY);
    }

    public ApiResponse sendRequestWithoutApiKey(String token, String action) {
        return executeRequest(token, action, null);
    }

    public ApiResponse sendRequestWithCustomApiKey(String token, String action, String apiKey) {
        return executeRequest(token, action, apiKey);
    }

    public Response sendRequestRaw(String token, String action) {
        Map<String, Object> formParams = createFormParams(token, action);
        return executeRawRequest(formParams, TestConfig.API_KEY);
    }

    private ApiResponse executeRequest(String token, String action, String apiKey) {
        Map<String, Object> formParams = createFormParams(token, action);

        try {
            Response response = executeRawRequest(formParams, apiKey);

            ApiResponse apiResponse = response.as(ApiResponse.class);
            apiResponse.setStatusCode(response.getStatusCode());

            logResponse(response, apiResponse);
            return apiResponse;

        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            ApiResponse errorResponse = new ApiResponse("ERROR", "Request failed: " + e.getMessage());
            errorResponse.setStatusCode(500);
            return errorResponse;
        }
    }

    private Response executeRawRequest(Map<String, Object> formParams, String apiKey) {
        var request = given()
                .config(RestAssured.config)
                .baseUri(TestConfig.BASE_URL)
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .formParams(formParams);

        if (apiKey != null && !apiKey.isEmpty()) {
            request.header("X-Api-Key", apiKey);
        }

        return request.when().post(TestConfig.ENDPOINT);
    }

    private Map<String, Object> createFormParams(String token, String action) {
        Map<String, Object> formParams = new HashMap<>();
        if (token != null && !token.isEmpty()) {
            formParams.put("token", token);
        }
        if (action != null && !action.isEmpty()) {
            formParams.put("action", action);
        }
        return formParams;
    }

    private void logResponse(Response response, ApiResponse apiResponse) {
        System.out.println("=== Response Details ===");
        System.out.println("HTTP Status: " + response.getStatusCode());
        System.out.println("Status Line: " + response.getStatusLine());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody().asString());
        System.out.println("ApiResponse: " + apiResponse);
        System.out.println("========================");
    }
}