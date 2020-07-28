package com.tsystems.tm.acc.wiremock.persist;

import com.github.jknack.handlebars.Template;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.Notifier;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.responsetemplating.RequestTemplateModel;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.google.common.collect.ImmutableMap;
import com.tsystems.tm.acc.wiremock.PostServeActionWithHandlebars;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;
import static com.github.tomakehurst.wiremock.core.WireMockApp.FILES_ROOT;
import static com.google.common.base.MoreObjects.firstNonNull;

public class PersistencePostServeAction extends PostServeActionWithHandlebars {
    public static final String NAME = "persist";
    private PersistenceService persistenceService;

    public PersistencePostServeAction() {
        super();
        persistenceService = PersistenceService.get();
    }

    public static PersistencePostServeActionDefinition persistence() {
        return new PersistencePostServeActionDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final PersistencePostServeActionDefinition definition = parameters.as(PersistencePostServeActionDefinition.class);
            doActionInternal(definition, serveEvent, admin, parameters);
        } catch (Throwable e) {
            e.printStackTrace();
            throwUnchecked(e);
        }
    }

    protected void doActionInternal(PersistencePostServeActionDefinition definition, ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final PersistencePostServeActionDefinition transformedDefinition = transform(serveEvent, definition, parameters, admin.getOptions());
        final Notifier notifier = notifier();

        try {
            switch (definition.getActionType()) {
                case set:
                    if (definition.getKey() != null) {
                        persistenceService.put(definition.getKey(), definition.getValue());
                    }
                    break;
                case clear:
                    persistenceService.clear();
                    break;
            }
        } catch (Throwable e) {
            throwUnchecked(e);
            notifier.error(e.toString());
        }
    }

    protected PersistencePostServeActionDefinition transform(ServeEvent serveEvent, PersistencePostServeActionDefinition definition, Parameters parameters, Options options) {
        final FileSource files = options.filesRoot().child(FILES_ROOT);
        final ImmutableMap<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("parameters", firstNonNull(parameters, Collections.<String, Object>emptyMap()))
                .put("request", RequestTemplateModel.from(serveEvent.getRequest())).build();

        if (definition.getValue() != null) {
            Template bodyTemplate = uncheckedCompileTemplate(definition.getValue());
            String newValue = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withValue(newValue);
        } else if (definition.getFileName() != null) {
            Template filePathTemplate = uncheckedCompileTemplate(definition.getFileName());
            String compiledFilePath = uncheckedApplyTemplate(filePathTemplate, model);
            TextFile file = files.getTextFileNamed(compiledFilePath);
            Template bodyTemplate = uncheckedCompileTemplate(file.readContentsAsString());
            String newValue = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withValue(newValue);
        }

        return definition;
    }
}
