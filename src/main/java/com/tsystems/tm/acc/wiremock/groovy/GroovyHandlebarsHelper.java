package com.tsystems.tm.acc.wiremock.groovy;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

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
                return this.handleError("You can not use both inline and scriptFilename");
            } else {
                return evalInline(inline.get(), inner, options);
            }
        } else if (scriptFilename.isPresent()) {
            return evalScriptFilename(scriptFilename.get(), inner, options);
        } else {
            return inner;
        }
    }

    private GroovyShell getShell(String inner, Options options) {
        Binding b = new Binding();
        b.setVariable("context", options.context);
        b.setVariable("inner", inner);
        b.setVariable("persistence", PersistenceService.get());
        options.hash.forEach(b::setVariable);
        return new GroovyShell(this.getClass().getClassLoader(), b);
    }

    private Object evalScriptFilename(String filename, String inner, Options options) throws IOException {
        return getShell(inner, options).parse(Paths.get(filesRoot.toString(), filename).toFile()).run();
    }

    private Object evalInline(String inline, String inner, Options options) {
        return getShell(inner, options).evaluate(inline);
    }
}
