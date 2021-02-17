package com.tsystems.tm.acc.wiremock.groovy;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class GroovyRequestMatcherExtension extends RequestMatcherExtension {
    private Path filesRoot;

    public GroovyRequestMatcherExtension() {
        filesRoot = Paths.get(Optional.ofNullable(System.getenv("GROOVY_ROOT")).orElse("./groovy"));
    }

    @Override
    public String getName() {
        return "withGroovy";
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {
        String inline = parameters.getString("inline");
        try {
            Binding b = new Binding();
            b.setVariable("request", request);
            b.setVariable("persistence", PersistenceService.get());

            StringBuilder script = new StringBuilder();
            File builtins = Paths.get(filesRoot.toString(), "builtin.groovy").toFile();
            if (builtins.exists()) {
                script.append(FileUtils.readFileToString(builtins));
                script.append("\n");
            }
            script.append(inline);
            return MatchResult.of((Boolean) new GroovyShell(this.getClass().getClassLoader(), b)
                    .parse(script.toString())
                    .run());
        } catch (Throwable e) {
            e.printStackTrace();
            return MatchResult.noMatch();
        }
    }
}
