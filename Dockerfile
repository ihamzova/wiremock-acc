FROM mtr.external.otc.telekomcloud.com/digitalhub/zulu-openjdk-8:master

ENV WM_PACKAGE wiremock

RUN mkdir /$WM_PACKAGE

WORKDIR /$WM_PACKAGE

COPY target/wiremock-acc-deps.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE

EXPOSE 8080

ENTRYPOINT ["java","-jar","wiremock-acc-deps.jar"]
CMD ["--max-request-journal-entries", "1000", "--disable-gzip", "--global-response-templating", "--extensions", "com.tsystems.tm.acc.Webhooks,com.tsystems.tm.acc.WebhooksArray,com.tsystems.tm.acc.wiremock.groovy.GroovyResponseTemplateTransformer,com.tsystems.tm.acc.wiremock.persist.PersistenceResponseTemplateTransformer", "--permitted-system-keys", ".*"]
