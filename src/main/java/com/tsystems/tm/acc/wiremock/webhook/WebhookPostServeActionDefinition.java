package com.tsystems.tm.acc.wiremock.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WebhookPostServeActionDefinition {

    private String method;
    private String url;
    private List<HttpHeader> headers;
    private Body body = Body.none();
    private Long fixedDelayMilliseconds;
    private String bodyFileName;

    @JsonCreator
    public WebhookPostServeActionDefinition(@JsonProperty("method") String method,
                                            @JsonProperty("url") String url,
                                            @JsonProperty("headers") HttpHeaders headers,
                                            @JsonProperty("bodyFileName") String bodyFileName,
                                            @JsonProperty("body") String body,
                                            @JsonProperty("jsonBody") JsonNode jsonBody,
                                            @JsonProperty("base64Body") String base64Body,
                                            @JsonProperty("fixedDelayMilliseconds") Long fixedDelayMilliseconds) {
        this.method = method;
        this.url = url;
        this.headers = newArrayList(headers.all());
        this.bodyFileName = bodyFileName;
        this.body = Body.fromOneOf(null, body, jsonBody, base64Body);
        this.fixedDelayMilliseconds = fixedDelayMilliseconds;
    }

    public WebhookPostServeActionDefinition() {
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBodyFileName() {
        return bodyFileName;
    }

    public HttpHeaders getHeaders() {
        return new HttpHeaders(headers);
    }

    @JsonIgnore
    public String getBase64Body() {
        return body.isBinary() ? body.asBase64() : null;
    }

    public String getBody() {
        return body.isBinary() ? null : body.asString();
    }

    @JsonIgnore
    public byte[] getBinaryBody() {
        return body.asBytes();
    }

    public WebhookPostServeActionDefinition withMethod(String method) {
        this.method = method;
        return this;
    }

    public WebhookPostServeActionDefinition withBodyFilename(String bodyFileName) {
        this.bodyFileName = bodyFileName;
        return this;
    }

    public WebhookPostServeActionDefinition withUrl(String url) {
        this.url = url;
        return this;
    }

    public WebhookPostServeActionDefinition withHeaders(List<HttpHeader> headers) {
        this.headers = headers;
        return this;
    }

    public WebhookPostServeActionDefinition withHeader(String key, String... values) {
        if (headers == null) {
            headers = newArrayList();
        }

        headers.add(new HttpHeader(key, values));
        return this;
    }

    public WebhookPostServeActionDefinition withBody(String body) {
        this.body = new Body(body);
        return this;
    }

    public WebhookPostServeActionDefinition withJsonBody(JsonNode jsonBody) {
        this.body = Body.fromOneOf(null, null, jsonBody, null);
        return this;
    }


    public WebhookPostServeActionDefinition withBinaryBody(byte[] body) {
        this.body = new Body(body);
        return this;
    }

    public WebhookPostServeActionDefinition withFixedDelayMilliseconds(long delay) {
        this.fixedDelayMilliseconds = delay;
        return this;
    }

    @JsonIgnore
    public boolean specifiesTextBodyContent() {
        return body.isPresent() && !body.isBinary();
    }

    @JsonIgnore
    public boolean specifiesBodyFile() {
        return bodyFileName != null && !bodyFileName.isEmpty();
    }

    public Long getFixedDelayMilliseconds() {
        return fixedDelayMilliseconds;
    }
}
