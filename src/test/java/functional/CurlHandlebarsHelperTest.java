package functional;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tsystems.tm.acc.wiremock.CustomHelpersResponseTemplateTransformer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import testsupport.WireMockResponse;
import testsupport.WireMockTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CurlHandlebarsHelperTest {

    @Rule
    public WireMockRule rule = new WireMockRule(
            options()
                    .dynamicPort()
                    .extensions(new CustomHelpersResponseTemplateTransformer()));
    private WireMockTestClient client;

    @Before
    public void init() {
        reset();
        client = new WireMockTestClient(rule.port());
        WireMock.configureFor(rule.port());
        System.out.println("Target server port: " + rule.port());
    }

    @Test
    public void shouldLoadDataFromUrlAndFireJsonPathValue() {
        rule.stubFor(get(urlPathEqualTo("/remote-relations/1"))
                .willReturn(aResponse().withStatus(200)
                        .withBody("{ \"relationships\": [ { \"id\": \"2\" } ] }"))
        );
        rule.stubFor(get(urlPathEqualTo("/test-remote-json-path"))
                .willReturn(aResponse().withStatus(200)
                        .withBody("{ {{#assign 'relationData'}}{{#curl}}" +
                                "http://localhost:"+rule.port()+"/remote-relations/{{request.query.relationId}}" +
                                "{{/curl}}{{/assign}}" +
                                "\"value\":{{jsonPath relationData '$.relationships[0].id'}}" +
                                " }"))
        );

        WireMockResponse response = client.get("/test-remote-json-path?relationId=1");

        assertThat(response.content(), is("{ \"value\":2 }"));
    }

}
