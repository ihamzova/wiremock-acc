FROM mtr.external.otc.telekomcloud.com/digitalhub/zulu-openjdk-8:master

LABEL quay.expires-after=12w

ENV WM_PACKAGE wiremock

RUN mkdir /$WM_PACKAGE

WORKDIR /$WM_PACKAGE

COPY target/wiremock-acc-deps.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE
COPY example-stubs /$WM_PACKAGE/example-stubs
COPY bilateral_stubs /$WM_PACKAGE/bilateral_stubs

EXPOSE 8080

CMD java -jar wiremock-acc-deps.jar --max-request-journal-entries 1000 --disable-gzip --extensions com.tsystems.tm.acc.Webhooks,com.tsystems.tm.acc.WebhooksArray,com.tsystems.tm.acc.wiremock.CustomHelpersResponseTemplateTransformer,com.tsystems.tm.acc.wiremock.persist.endpoint.PersistenceAdminApi --root-dir $STUBS_PATH