package nord_codes.aqa.task.apitests.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import nord_codes.aqa.task.apitests.clients.ApiClient;
import nord_codes.aqa.task.apitests.config.WireMockExtension;
import nord_codes.aqa.task.apitests.utils.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@TestMethodOrder(TestOrder.class)
public class BaseTest {

    @RegisterExtension
    static WireMockExtension wireMockExtension = new WireMockExtension(8888);

    protected final ApiClient apiClient = new ApiClient();
    protected final TokenGenerator tokenGenerator = new TokenGenerator();

    protected WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        this.wireMockServer = wireMockExtension.getServer();

        if (wireMockExtension.isRunning()) {
            WireMock.reset();
            setupDefaultStubs();
            System.out.println("WireMock configured and ready");
        } else {
            System.err.println("WireMock is not running!");
        }
    }

    protected void setupDefaultStubs() {
        stubFor(
                post(urlEqualTo("/auth"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/plain")
                                .withBody("OK"))
        );

        stubFor(
                post(urlEqualTo("/doAction"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/plain")
                                .withBody("OK"))
        );
    }

    protected void setupExternalServiceFailure(String endpoint, int statusCode) {
        stubFor(
                post(urlEqualTo(endpoint))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "text/plain")
                                .withBody("ERROR"))
        );
    }

    protected void setupExternalServiceSuccess(String endpoint) {
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"OK\"}")));
    }

    protected void verifyExternalServiceCalled(String endpoint, String token) {
        verify(
                postRequestedFor(urlEqualTo(endpoint))
                        .withRequestBody(containing("token=" + token))
        );
    }

    protected void verifyExternalServiceNotCalled(String endpoint) {
        verify(0, postRequestedFor(urlEqualTo(endpoint)));
    }

    protected boolean isWireMockRunning() {
        return wireMockExtension != null && wireMockExtension.isRunning();
    }

    protected void setupExternalServiceTimeout(String endpoint) {
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(5000) // 5 seconds delay
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"OK\"}")));
    }

    protected void verifyExternalServiceCalledTimes(String endpoint, int times) {
        verify(times, postRequestedFor(urlEqualTo(endpoint)));
    }
}