package com.tsystems.tm.acc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.NumberHelper;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.WireMockHelpers;
import com.tsystems.tm.acc.wiremock.env.UnsafeEnvHandlebarsHelper;
import com.tsystems.tm.acc.wiremock.groovy.GroovyHandlebarsHelper;
import com.tsystems.tm.acc.wiremock.oauth.Oauth2Helper;
import com.tsystems.tm.acc.wiremock.persist.PersistenceHandlebarsHelper;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public abstract class PostServeActionWithHandlebars extends PostServeAction {
    protected final Handlebars handlebars;

    public PostServeActionWithHandlebars() {
        handlebars = new Handlebars();
        //Add all available wiremock helpers
        for (WireMockHelpers helper : WireMockHelpers.values()) {
            this.handlebars.registerHelper(helper.name(), helper);
        }
        for (StringHelpers helper : StringHelpers.values()) {
            this.handlebars.registerHelper(helper.name(), helper);
        }
        for (NumberHelper helper : NumberHelper.values()) {
            this.handlebars.registerHelper(helper.name(), helper);
        }
        for (ConditionalHelpers helper : ConditionalHelpers.values()) {
            this.handlebars.registerHelper(helper.name(), helper);
        }
        this.handlebars.registerHelper(AssignHelper.NAME, new AssignHelper());
        this.handlebars.registerHelper(UnsafeEnvHandlebarsHelper.NAME, new UnsafeEnvHandlebarsHelper());
        this.handlebars.registerHelper("oauth", new Oauth2Helper());
        this.handlebars.registerHelper(GroovyHandlebarsHelper.NAME, new GroovyHandlebarsHelper());
        this.handlebars.registerHelper(PersistenceHandlebarsHelper.NAME, new PersistenceHandlebarsHelper());
    }

    protected String uncheckedApplyTemplate(Template template, Object context) {
        try {
            return template.apply(context);
        } catch (IOException e) {
            return throwUnchecked(e, String.class);
        }
    }

    protected Template uncheckedCompileTemplate(String content) {
        try {
            return handlebars.compileInline(content);
        } catch (IOException e) {
            return throwUnchecked(e, Template.class);
        }
    }
}
