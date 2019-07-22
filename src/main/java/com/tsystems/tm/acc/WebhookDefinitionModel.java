package com.tsystems.tm.acc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.RequestMethod;

import java.io.IOException;
import java.util.List;

public class WebhookDefinitionModel {
    private RequestMethod method;
    private String url;
    private List<HttpHeader> headers;
    private Object jsonBody;
    private Integer fixedDelayMilliseconds;
    private String bodyFileName;

    public WebhookDefinitionModel(RequestMethod method, String url, List<HttpHeader> headers, Body body, Integer fixedDelayMilliseconds, String bodyFileName) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        try {
            this.jsonBody = body.isBinary() ? null : new ObjectMapper().readValue(body.asString(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
            this.jsonBody = null;
        }
        this.fixedDelayMilliseconds = fixedDelayMilliseconds;
        this.bodyFileName = bodyFileName;
    }
}
