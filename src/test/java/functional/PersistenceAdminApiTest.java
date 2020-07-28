package functional;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tsystems.tm.acc.wiremock.persist.PersistencePostServeAction;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import com.tsystems.tm.acc.wiremock.persist.endpoint.PersistenceAdminApi;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.PersistenceResult;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.data.PersistencePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testsupport.WireMockResponse;
import testsupport.WireMockTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.net.HttpURLConnection.*;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;


public class PersistenceAdminApiTest {
    @Rule
    public WireMockRule targetServer = new WireMockRule(options().dynamicPort());

    PersistencePostServeAction persistencePostServeAction = new PersistencePostServeAction();
    PersistenceAdminApi persistenceAdminApi = new PersistenceAdminApi();
    @Rule
    public WireMockRule rule = new WireMockRule(
            options()
                    .dynamicPort()
                    .extensions(persistencePostServeAction, persistenceAdminApi));
    WireMockTestClient client;

    @Before
    public void init() {
        reset();
        PersistenceService.get().clear();
        targetServer.stubFor(any(anyUrl())
                .willReturn(aResponse().withStatus(200)));
        client = new WireMockTestClient(rule.port());
        WireMock.configureFor(targetServer.port());

        System.out.println("Target server port: " + targetServer.port());
        System.out.println("Under test server port: " + rule.port());
    }

    private void populatePersistenceValues() {
        PersistenceService.get().put("testKey1", "testValue1");
        PersistenceService.get().put("testKey2", "testValue2");
        PersistenceService.get().put("testKey3", "testValue3");
    }

    @Test
    public void persistenceGetAllValuesTest() {
        populatePersistenceValues();
        WireMockResponse wireMockResponse = client.get("/__admin/persistence");
        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_OK);
        PersistenceResult data = Json.read(wireMockResponse.content(), PersistenceResult.class);
        Assert.assertEquals(data.getValues().size(), 3);
    }

    @Test
    public void persistenceGetSpecificValueTest() {
        populatePersistenceValues();
        WireMockResponse wireMockResponse = client.get("/__admin/persistence/testKey1");
        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_OK);
        PersistencePair value = Json.read(wireMockResponse.content(), PersistencePair.class);

        Assert.assertEquals(value.getName(), "testKey1");
        Assert.assertEquals(value.getValue(), "testValue1");
    }

    @Test
    public void persistencePutSpecificValueTest() {
        final String testKey = "testKey1";
        final String newTestValue = "testValue1000";
        populatePersistenceValues();
        PersistencePair newPair = new PersistencePair(testKey, newTestValue);
        WireMockResponse wireMockResponse = client.putWithBody("/__admin/persistence",
                Json.write(newPair),
                APPLICATION_JSON.getMimeType());

        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_CREATED);
        Assert.assertEquals(PersistenceService.get().get(testKey), newTestValue);
    }

    @Test
    public void persistenceCreateNewValueTest() {
        final String testKey = "testKey2000";
        final String testValue = "testValue2000";
        populatePersistenceValues();
        PersistencePair newPair = new PersistencePair(testKey, testValue);
        WireMockResponse wireMockResponse = client.postJson("/__admin/persistence",
                Json.write(newPair));

        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_CREATED);
        Assert.assertEquals(PersistenceService.get().get(testKey), testValue);
    }

    @Test
    public void deleteSingleValueTest() {
        final String testKey = "testKey1";
        populatePersistenceValues();

        WireMockResponse wireMockResponse = client.delete("/__admin/persistence/" + testKey);

        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_NO_CONTENT);
        Assert.assertNull(PersistenceService.get().get(testKey));
    }

    @Test
    public void deleteAllValuesTest() {
        populatePersistenceValues();

        WireMockResponse wireMockResponse = client.delete("/__admin/persistence");

        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_NO_CONTENT);

        wireMockResponse = client.get("/__admin/persistence");
        Assert.assertEquals(wireMockResponse.statusCode(), HTTP_OK);
        PersistenceResult data = Json.read(wireMockResponse.content(), PersistenceResult.class);
        Assert.assertEquals(data.getValues().size(), 0);
    }
}
