package com.tsystems.tm.acc.wiremock;

import com.github.jknack.handlebars.Helper;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.tsystems.tm.acc.wiremock.env.UnsafeEnvHandlebarsHelper;
import com.tsystems.tm.acc.wiremock.groovy.GroovyHandlebarsHelper;
import com.tsystems.tm.acc.wiremock.persist.PersistenceHandlebarsHelper;

import java.util.HashMap;

public class CustomHelpersResponseTemplateTransformer extends ResponseTemplateTransformer {
    public CustomHelpersResponseTemplateTransformer() {
        super(true, new HashMap<String, Helper>() {{
            put(UnsafeEnvHandlebarsHelper.NAME, new UnsafeEnvHandlebarsHelper());
            put(GroovyHandlebarsHelper.NAME, new GroovyHandlebarsHelper());
            put(PersistenceHandlebarsHelper.NAME, new PersistenceHandlebarsHelper());
        }});
    }

    public String getName() {
        return "customHelpers";
    }
}
