package com.tsystems.tm.acc.wiremock.webhook;

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
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.google.common.collect.ImmutableMap;
import com.tsystems.tm.acc.wiremock.AsyncPostServeActionWithHandlebars;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;
import static com.github.tomakehurst.wiremock.core.WireMockApp.FILES_ROOT;
import static com.github.tomakehurst.wiremock.http.HttpClientFactory.getHttpRequestFor;
import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WebhookPostServeAction extends AsyncPostServeActionWithHandlebars {
    public static final String NAME = "webhook";
    protected final HttpClient httpClient;

    public WebhookPostServeAction() {
        super();
        httpClient = HttpClientFactory.createClient();
    }

    protected static HttpUriRequest buildRequest(WebhookPostServeActionDefinition definition) {
        HttpUriRequest request = getHttpRequestFor(
                RequestMethod.fromString(definition.getMethod()),
                definition.getUrl()
        );

        for (HttpHeader header : definition.getHeaders().all()) {
            request.addHeader(header.key(), header.firstValue());
        }

        if (RequestMethod.fromString(definition.getMethod()).hasEntity()) {
            HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) request;
            entityRequest.setEntity(new ByteArrayEntity(definition.getBinaryBody()));
        }

        return request;
    }

    public static WebhookPostServeActionDefinition webhook() {
        return new WebhookPostServeActionDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final WebhookPostServeActionDefinition definition = parameters.as(WebhookPostServeActionDefinition.class);
            doActionInternal(definition, serveEvent, admin, parameters);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void doActionInternal(WebhookPostServeActionDefinition definition, ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final WebhookPostServeActionDefinition transformedDefinition = transform(serveEvent, definition, parameters, admin.getOptions());
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

    protected WebhookPostServeActionDefinition transform(ServeEvent serveEvent, WebhookPostServeActionDefinition definition, Parameters parameters, Options options) {
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

        if (definition.getMethod() != null) {
            Template template = uncheckedCompileTemplate(definition.getMethod());
            String newUrl = uncheckedApplyTemplate(template, model);
            definition.withMethod(newUrl);
        }

        return definition;
    }
}
