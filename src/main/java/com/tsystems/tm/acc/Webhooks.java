package com.tsystems.tm.acc;

import com.github.jknack.handlebars.Template;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.Notifier;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.responsetemplating.RequestTemplateModel;
import com.github.tomakehurst.wiremock.http.HttpClientFactory;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;
import static com.github.tomakehurst.wiremock.core.WireMockApp.FILES_ROOT;
import static com.github.tomakehurst.wiremock.http.HttpClientFactory.getHttpRequestFor;
import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Webhooks extends AsyncPostServeActionWithHandlebars {
    public static final String NAME = "webhook";
    protected final HttpClient httpClient;

    public Webhooks() {
        super();
        httpClient = HttpClientFactory.createClient();
    }

    protected static HttpUriRequest buildRequest(WebhookDefinition definition) {
        HttpUriRequest request = getHttpRequestFor(
                definition.getMethod(),
                definition.getUrl()
        );

        for (HttpHeader header : definition.getHeaders().all()) {
            request.addHeader(header.key(), header.firstValue());
        }

        if (definition.getMethod().hasEntity()) {
            HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) request;
            entityRequest.setEntity(new ByteArrayEntity(definition.getBinaryBody()));
        }

        return request;
    }

    public static WebhookDefinition webhook() {
        return new WebhookDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final WebhookDefinition definition = parameters.as(WebhookDefinition.class);
            doActionInternal(definition, serveEvent, admin, parameters);
        } catch (Throwable e) {
            e.printStackTrace();
            throwUnchecked(e);
        }
    }

    protected void doActionInternal(WebhookDefinition definition, ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final WebhookDefinition transformedDefinition = transform(serveEvent, definition, parameters, admin.getOptions());
        final Notifier notifier = notifier();

        scheduler.schedule(
                () -> {
                    try {
                        HttpUriRequest request = buildRequest(transformedDefinition);
                        HttpResponse response = httpClient.execute(request);
                        notifier.info(
                                String.format("Webhook %s request to %s returned status %s\n\n%s",
                                        transformedDefinition.getMethod(),
                                        transformedDefinition.getUrl(),
                                        response.getStatusLine(),
                                        EntityUtils.toString(response.getEntity())
                                )
                        );
                    } catch (Throwable e) {
                        throwUnchecked(e);
                        notifier.error(e.toString());
                    }
                },
                transformedDefinition.getFixedDelayMilliseconds() != null ? transformedDefinition.getFixedDelayMilliseconds() : 0L,
                MILLISECONDS
        );
    }

    protected WebhookDefinition transform(ServeEvent serveEvent, WebhookDefinition definition, Parameters parameters, Options options) {
        final FileSource files = options.filesRoot().child(FILES_ROOT);
        final ImmutableMap<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("parameters", firstNonNull(parameters, Collections.<String, Object>emptyMap()))
                .put("request", RequestTemplateModel.from(serveEvent.getRequest())).build();

        if (definition.specifiesTextBodyContent()) {
            Template bodyTemplate = uncheckedCompileTemplate(definition.getBody());
            String newBody = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withBody(newBody);
        } else if (definition.specifiesBodyFile()) {
            Template filePathTemplate = uncheckedCompileTemplate(definition.getBodyFileName());
            String compiledFilePath = uncheckedApplyTemplate(filePathTemplate, model);
            TextFile file = files.getTextFileNamed(compiledFilePath);
            Template bodyTemplate = uncheckedCompileTemplate(file.readContentsAsString());
            String newBody = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withBody(newBody);
        }

        if (definition.getHeaders() != null) {
            List<HttpHeader> newResponseHeaders = definition.getHeaders().all().stream().map(header -> {
                List<String> newValues = header.values().stream().map(str -> {
                    Template template = uncheckedCompileTemplate(str);
                    return uncheckedApplyTemplate(template, model);
                }).collect(Collectors.toList());

                return new HttpHeader(header.key(), newValues);
            }).collect(Collectors.toList());
            definition.withHeaders(newResponseHeaders);
        }

        if (definition.getUrl() != null) {
            Template template = uncheckedCompileTemplate(definition.getUrl());
            String newUrl = uncheckedApplyTemplate(template, model);
            definition.withUrl(newUrl);
        }

        return definition;
    }
}
