package com.tsystems.tm.acc.wiremock.env;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;
import org.apache.commons.lang3.StringUtils;


public class UnsafeEnvHandlebarsHelper extends HandlebarsHelper<Object> {
    public static final String NAME = "unsafeSystemValue";

    @Override
    public String apply(Object context, Options options) {
        String key = options.hash("key", "");
        String type = options.hash("type", "ENVIRONMENT");
        if (StringUtils.isEmpty(key)) {
            return this.handleError("The key cannot be empty");
        }

        String rawValue = "";

        switch (type) {
            case "ENVIRONMENT":
                rawValue = getSystemEnvironment(key);
                break;
            case "PROPERTY":
                rawValue = getSystemProperties(key);
                break;
        }
        return rawValue;

    }

    private String getSystemEnvironment(final String key) {
        return System.getenv(key);
    }

    private String getSystemProperties(final String key) {
        return System.getProperty(key);
    }
}
