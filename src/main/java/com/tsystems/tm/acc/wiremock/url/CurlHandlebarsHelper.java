package com.tsystems.tm.acc.wiremock.url;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CurlHandlebarsHelper extends HandlebarsHelper<Object> {
    public static final String NAME = "curl";

    @Override
    public Object apply(Object context, Options options) throws IOException {
        String innerUrl = options.apply(options.fn).toString().trim();

        if (StringUtils.isEmpty(innerUrl)) {
            return handleError("The URL cannot be empty");
        }

        HttpGet request = new HttpGet(innerUrl);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        }
        return handleError(String.format("Data can't be loaded for URL: %s", innerUrl));
    }
}

