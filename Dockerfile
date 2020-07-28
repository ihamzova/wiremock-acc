FROM mtr.external.otc.telekomcloud.com/digitalhub/zulu-openjdk-8:master

LABEL quay.expires-after=12w

ENV WM_PACKAGE wiremock

RUN mkdir /$WM_PACKAGE

WORKDIR /$WM_PACKAGE

COPY target/wiremock-acc-deps.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE
COPY example-stubs /$WM_PACKAGE/example-stubs
COPY bilateral_stubs /$WM_PACKAGE/bilateral_stubs
COPY avengers-stubs /$WM_PACKAGE/avengers-stubs

EXPOSE 8080

CMD java -jar wiremock-acc-deps.jar --max-request-journal-entries 1000 --disable-gzip --extensions com.tsystems.tm.acc.wiremock.webhook.WebhookPostServeAction,com.tsystems.tm.acc.wiremock.webhook.WebhooksPostServeAction,com.tsystems.tm.acc.wiremock.CustomHelpersResponseTemplateTransformer,com.tsystems.tm.acc.wiremock.persist.endpoint.PersistenceAdminApi --root-dir $STUBS_PATH