package functional;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tsystems.tm.acc.wiremock.webhook.WebhookPostServeAction;
import com.tsystems.tm.acc.wiremock.webhook.WebhooksPostServeAction;
import com.tsystems.tm.acc.wiremock.groovy.GroovyPostServeAction;
import com.tsystems.tm.acc.wiremock.persist.ActionType;
import com.tsystems.tm.acc.wiremock.persist.PersistencePostServeAction;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import org.apache.http.entity.StringEntity;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testsupport.TestHttpHeader;
import testsupport.TestNotifier;
import testsupport.WireMockTestClient;

import java.util.concurrent.CountDownLatch;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.http.RequestMethod.GET;
import static com.github.tomakehurst.wiremock.http.RequestMethod.POST;
import static com.tsystems.tm.acc.wiremock.webhook.WebhookPostServeAction.webhook;
import static com.tsystems.tm.acc.wiremock.webhook.WebhooksPostServeAction.webhooks;
import static com.tsystems.tm.acc.wiremock.groovy.GroovyPostServeAction.groovy;
import static com.tsystems.tm.acc.wiremock.persist.PersistencePostServeAction.persistence;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WebhookPostServeActionAcceptanceTest {
    @Rule
    public WireMockRule targetServer = new WireMockRule(options().dynamicPort());

    CountDownLatch latch;
    WebhookPostServeAction webhookPostServeAction = new WebhookPostServeAction();
    WebhooksPostServeAction webhooksArray = new WebhooksPostServeAction();
    PersistencePostServeAction persistencePostServeAction = new PersistencePostServeAction();
    GroovyPostServeAction groovyPostServeAction = new GroovyPostServeAction();
    TestNotifier notifier = new TestNotifier();
    @Rule
    public WireMockRule rule = new WireMockRule(
            options()
                    .dynamicPort()
                    .notifier(notifier)
                    .extensions(webhookPostServeAction, webhooksArray, persistencePostServeAction, groovyPostServeAction));
    WireMockTestClient client;

    @Before
    public void init() {
        targetServer.addMockServiceRequestListener((request, response) -> {
            if (request.getUrl().startsWith("/callback")) {
                latch.countDown();
            }
        });
        reset();
        notifier.reset();
        targetServer.stubFor(any(anyUrl())
                .willReturn(aResponse().withStatus(200)));
        latch = new CountDownLatch(1);
        client = new WireMockTestClient(rule.port());
        WireMock.configureFor(targetServer.port());

        System.out.println("Target server port: " + targetServer.port());
        System.out.println("Under test server port: " + rule.port());
    }

    @Test
    public void firesASingleWebhookWhenRequested() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/something-async"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("webhook", webhook()
                        .withMethod("POST")
                        .withUrl("http://localhost:" + targetServer.port() + "/callback")
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"result\": \"SUCCESS\" }"))
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/something-async", new StringEntity("", TEXT_PLAIN));

        waitForRequestToTargetServer();

        verify(1, postRequestedFor(urlEqualTo("/callback"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("{ \"result\": \"SUCCESS\" }"))
        );

        assertThat(notifier.getInfoMessages(), hasItem(allOf(
                containsString("Webhook POST request to"),
                containsString("/callback returned status"),
                containsString("200")
        )));
    }

    @Test
    public void firesMinimalWebhook() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/something-async"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("webhook", webhook()
                        .withMethod("GET")
                        .withUrl("http://localhost:" + targetServer.port() + "/callback"))
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/something-async", new StringEntity("", TEXT_PLAIN));

        waitForRequestToTargetServer();

        verify(1, getRequestedFor(urlEqualTo("/callback")));
    }

    @Test
    public void firesASingleWebhookWhenRequestedWithTransform() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/something-async"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("webhook", webhook()
                        .withMethod("POST")
                        .withUrl("{{request.headers.X-Callback-Url}}")
                        .withHeader("Content-Type", "application/json")
                        .withBody("{{request.body}}"))
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/something-async", new StringEntity("TEST", TEXT_PLAIN), new TestHttpHeader("X-Callback-Url", "http://localhost:" + targetServer.port() + "/callback"));

        waitForRequestToTargetServer();

        verify(1, postRequestedFor(urlEqualTo("/callback"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalTo("TEST"))
        );

        assertThat(notifier.getInfoMessages(), hasItem(allOf(
                containsString("Webhook POST request to"),
                containsString("/callback returned status"),
                containsString("200")
        )));
    }

    @Test
    public void firesMultipleWebhooks() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/something-async"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("webhooks", webhooks()
                        .addWebhook(webhook()
                                .withMethod("GET")
                                .withUrl("http://localhost:" + targetServer.port() + "/callback1"))
                        .addWebhook(webhook()
                                .withMethod("GET")
                                .withUrl("http://localhost:" + targetServer.port() + "/callback2"))
                )
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/something-async", new StringEntity("", TEXT_PLAIN));

        waitForRequestToTargetServer();

        verify(1, getRequestedFor(urlEqualTo("/callback1")));
        verify(1, getRequestedFor(urlEqualTo("/callback2")));
    }

    @Test
    public void persistenceTest() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/persist"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("persist", persistence()
                        .withActionType(ActionType.set)
                        .withKey("testKey")
                        .withValue("testValue")
                )
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/persist", new StringEntity("", TEXT_PLAIN));

        sleep(1000);

        assertThat(PersistenceService.get().get("testKey"), Matchers.equalTo("testValue"));
    }

    @Test
    public void groovyTest() throws Exception {
        rule.stubFor(post(urlPathEqualTo("/groovy"))
                .willReturn(aResponse().withStatus(200))
                .withPostServeAction("groovy", groovy()
                        .withInline("persistence.put(\"testKey\", new String(serveEvent.getRequest().getBody()))")
                )
        );

        verify(0, postRequestedFor(anyUrl()));

        client.post("/groovy", new StringEntity("testValue", TEXT_PLAIN));

        sleep(1000);

        assertThat(PersistenceService.get().get("testKey"), Matchers.equalTo("testValue"));
    }

    private void waitForRequestToTargetServer() throws Exception {
        latch.await(2, SECONDS);
        assertThat("Timed out waiting for target server to receive a request",
                latch.getCount(), is(0L));
    }

}
