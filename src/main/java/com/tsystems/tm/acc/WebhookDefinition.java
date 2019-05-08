package com.tsystems.tm.acc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.RequestMethod;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WebhookDefinition {

    private RequestMethod method;
    private String url;
    private List<HttpHeader> headers;
    private Body body = Body.none();
    private Integer fixedDelayMilliseconds;
    private String bodyFileName;

    @JsonCreator
    public WebhookDefinition(@JsonProperty("method") RequestMethod method,
                             @JsonProperty("url") String url,
                             @JsonProperty("headers") HttpHeaders headers,
                             @JsonProperty("bodyFileName") String bodyFileName,
                             @JsonProperty("body") String body,
                             @JsonProperty("jsonBody") JsonNode jsonBody,
                             @JsonProperty("base64Body") String base64Body,
                             @JsonProperty("fixedDelayMilliseconds") Integer fixedDelayMilliseconds) {
        this.method = method;
        this.url = url;
        this.headers = newArrayList(headers.all());
        this.bodyFileName = bodyFileName;
        this.body = Body.fromOneOf(null, body, jsonBody, base64Body);
        this.fixedDelayMilliseconds = fixedDelayMilliseconds;
    }

    public WebhookDefinition() {
    }

    public RequestMethod getMethod() {
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

    public WebhookDefinition withMethod(RequestMethod method) {
        this.method = method;
        return this;
    }

    public WebhookDefinition withBodyFilename(String bodyFileName) {
        this.bodyFileName = bodyFileName;
        return this;
    }

    public WebhookDefinition withUrl(String url) {
        this.url = url;
        return this;
    }

    public WebhookDefinition withHeaders(List<HttpHeader> headers) {
        this.headers = headers;
        return this;
    }

    public WebhookDefinition withHeader(String key, String... values) {
        if (headers == null) {
            headers = newArrayList();
        }

        headers.add(new HttpHeader(key, values));
        return this;
    }

    public WebhookDefinition withBody(String body) {
        this.body = new Body(body);
        return this;
    }

    public WebhookDefinition withBinaryBody(byte[] body) {
        this.body = new Body(body);
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
}
