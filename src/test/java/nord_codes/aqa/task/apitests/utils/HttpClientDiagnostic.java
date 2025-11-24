package nord_codes.aqa.task.apitests.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientDiagnostic {
    WireMockServer wireMockServer;

    public static void testConnection(String urlString, String token, String action) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Api-Key", "qazWSXedc");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String formData = "token=" + token + "&action=" + action;

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = formData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWiremock() {
        //wireMockServer = new WireMockServer(8081);
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8081)
                .notifier(new ConsoleNotifier(true))
        );
        setupPositiveStubs();
        wireMockServer.start();
        wireMockServer.isRunning();
    }

    public void setupPositiveStubs() {
        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/auth"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"OK\"}"))
        );

        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/doAction"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"OK\"}"))
        );
    }

    public void setupNegativeStubs() {
        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/auth"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"OK\"}"))
        );

        wireMockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/doAction"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"status\":\"OK\"}"))
        );
    }

    public static void main(String[] args) {
        new HttpClientDiagnostic().checkWiremock();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //WireMockConfig c = new WireMockConfig();
        //WireMock.reset();
        //HttpClientDiagnostic.testConnection("http://localhost:8080/endpoint/", TokenGenerator.generateValidToken(), "LOGIN");
    }
}