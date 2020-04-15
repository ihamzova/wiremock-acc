package com.tsystems.tm.acc.wiremock.persist;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;

import java.io.IOException;
import java.util.Optional;

public class PersistenceHandlebarsHelper extends HandlebarsHelper<Object> {
    public static final String NAME = "persist";
    private PersistenceService persistenceService;

    public PersistenceHandlebarsHelper() {
        persistenceService = PersistenceService.get();
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        String inner = options.apply(options.fn).toString().trim();

        Optional<String> key = Optional.ofNullable(options.hash("key"));

        if (key.isPresent()) {
            return persistenceService.get(key.get());
        } else {
            return inner;
        }
    }
}
