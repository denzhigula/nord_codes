/*
 * Copyright 2022 WANdisco.
 */

package nord_codes.aqa.task.apitests.config;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;

public class WireMockExtension implements BeforeEachCallback, AfterEachCallback {
    private WireMockServer wireMockServer;
    private final int port;

    public WireMockExtension(int port) {
        this.port = port;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        wireMockServer = new WireMockServer(
                wireMockConfig()
                        .port(port)
                        .bindAddress("localhost")
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", port);

        System.out.println("WireMock started on port: " + port);

        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .put("wireMockServer", wireMockServer);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            System.out.println("WireMock stopped");
        }
    }

    public WireMockServer getServer() {
        return wireMockServer;
    }

    public String getBaseUrl() {
        return "http://localhost:" + port;
    }

    public boolean isRunning() {
        return wireMockServer != null && wireMockServer.isRunning();
    }

    public static WireMockServer getWireMockServer(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL)
                .get("wireMockServer", WireMockServer.class);
    }
}