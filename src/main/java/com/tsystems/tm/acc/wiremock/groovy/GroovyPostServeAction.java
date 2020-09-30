package com.tsystems.tm.acc.wiremock.groovy;

import com.github.jknack.handlebars.Template;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.Notifier;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.responsetemplating.RequestTemplateModel;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.google.common.collect.ImmutableMap;
import com.tsystems.tm.acc.wiremock.PostServeActionWithHandlebars;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;
import static com.google.common.base.MoreObjects.firstNonNull;

public class GroovyPostServeAction extends PostServeActionWithHandlebars {
    public static final String NAME = "groovy";
    private Path filesRoot;

    public GroovyPostServeAction() {
        filesRoot = Paths.get(Optional.ofNullable(System.getenv("GROOVY_ROOT")).orElse("./groovy"));
    }

    public static GroovyPostServeActionDefinition groovy() {
        return new GroovyPostServeActionDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final GroovyPostServeActionDefinition definition = parameters.as(GroovyPostServeActionDefinition.class);
            doActionInternal(definition, serveEvent, admin, parameters);
        } catch (Throwable e) {
            e.printStackTrace();
            throwUnchecked(e);
        }
    }

    protected void doActionInternal(GroovyPostServeActionDefinition definition, ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final GroovyPostServeActionDefinition transformedDefinition = transform(serveEvent, definition, parameters, admin.getOptions());
        final Notifier notifier = notifier();

        try {
            Binding b = new Binding();
            b.setVariable("admin", admin);
            b.setVariable("serveEvent", serveEvent);
            b.setVariable("persistence", PersistenceService.get());
            transformedDefinition.getArguments().forEach(b::setVariable);

            StringBuilder script = new StringBuilder();
            File builtins = Paths.get(filesRoot.toString(), "builtin.groovy").toFile();
            if (builtins.exists()) {
                script.append(FileUtils.readFileToString(builtins));
                script.append("\n");
            }
            script.append(transformedDefinition.getInline());
            new GroovyShell(this.getClass().getClassLoader(), b)
                    .parse(script.toString())
                    .run();
        } catch (Throwable e) {
            throwUnchecked(e);
            notifier.error(e.toString());
        }
    }

    protected GroovyPostServeActionDefinition transform(ServeEvent serveEvent, GroovyPostServeActionDefinition definition, Parameters parameters, Options options) {
        final FileSource files = new SingleRootFileSource(filesRoot.toFile());
        final ImmutableMap<String, Object> model = ImmutableMap.<String, Object>builder()
                .put("parameters", firstNonNull(parameters, Collections.<String, Object>emptyMap()))
                .put("request", RequestTemplateModel.from(serveEvent.getRequest())).build();

        if (definition.getInline() != null) {
            Template bodyTemplate = uncheckedCompileTemplate(definition.getInline());
            String newValue = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withInline(newValue);
        } else if (definition.getScriptFileName() != null) {
            Template filePathTemplate = uncheckedCompileTemplate(definition.getScriptFileName());
            String compiledFilePath = uncheckedApplyTemplate(filePathTemplate, model);
            TextFile file = files.getTextFileNamed(compiledFilePath);
            Template bodyTemplate = uncheckedCompileTemplate(file.readContentsAsString());
            String newValue = uncheckedApplyTemplate(bodyTemplate, model);
            definition.withInline(newValue);
        }

        if (definition.getArguments() != null) {
            Map<String, Object> newArguments = new HashMap<>();
            for (Map.Entry<String, Object> entry : definition.getArguments().entrySet()) {
                Template bodyTemplate = uncheckedCompileTemplate(entry.getValue().toString());
                String newValue = uncheckedApplyTemplate(bodyTemplate, model);
                newArguments.put(entry.getKey(), newValue);
            }
            definition.withArguments(newArguments);
        }

        return definition;
    }
}
