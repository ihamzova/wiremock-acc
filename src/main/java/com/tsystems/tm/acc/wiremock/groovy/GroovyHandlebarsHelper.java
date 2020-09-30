package com.tsystems.tm.acc.wiremock.groovy;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class GroovyHandlebarsHelper extends HandlebarsHelper<Object> {
    public static final String NAME = "groovy";
    private Path filesRoot;

    public GroovyHandlebarsHelper() {
        filesRoot = Paths.get(Optional.ofNullable(System.getenv("GROOVY_ROOT")).orElse("./groovy"));
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        String inner = options.apply(options.fn).toString().trim();

        Optional<String> inline = Optional.ofNullable(options.hash("inline"));
        Optional<String> scriptFilename = Optional.ofNullable(options.hash("scriptFilename"));

        if (inline.isPresent()) {
            if (scriptFilename.isPresent()) {
                return handleError("You can not use both inline and scriptFilename");
            } else if (!StringUtils.isEmpty(inner)) {
                return handleError("You can not use both inline and inner script");
            } else {
                return evalInline(inline.get(), options);
            }
        } else if (!StringUtils.isEmpty(inner)) {
            if (scriptFilename.isPresent()) {
                return handleError("You can not use both inner script and scriptFilename");
            } else {
                return evalInline(inner, options);
            }
        } else if (scriptFilename.isPresent()) {
            return evalInline(FileUtils.readFileToString(Paths.get(filesRoot.toString(), scriptFilename.get()).toFile()), options);
        } else {
            return inner;
        }
    }

    private GroovyShell getShell(Options options) throws IOException {
        Binding b = new Binding();
        b.setVariable("context", options.context);
        b.setVariable("persistence", PersistenceService.get());
        options.hash.forEach(b::setVariable);
        return new GroovyShell(this.getClass().getClassLoader(), b);
    }

    private Object evalInline(String inline, Options options) throws IOException {
        StringBuilder script = new StringBuilder();
        File builtins = Paths.get(filesRoot.toString(), "builtin.groovy").toFile();
        if (builtins.exists()) {
            script.append(FileUtils.readFileToString(builtins));
            script.append("\n");
        }
        script.append(inline);
        return getShell(options).evaluate(script.toString());
    }
}
