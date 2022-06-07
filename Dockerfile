ARG GV_REGISTRY_ADDRESS
ARG GV_REGISTRY_NAMESPACE

FROM $GV_REGISTRY_ADDRESS/$GV_REGISTRY_NAMESPACE/oraclelinux8-zulu-openjdk-8:master

LABEL quay.expires-after=12w

ENV WM_PACKAGE wiremock
ENV STUBS_PATH="."
ENV CONTAINER_THREADS="10"
ENV ASYNC_RESPONSE_THREADS="10"
ENV ADDITIONAL_OPTIONS=""
ENV MAX_REQUEST_JOURNAL_ENTRIES="1000"

RUN mkdir /$WM_PACKAGE

WORKDIR /$WM_PACKAGE

COPY target/wiremock-acc-deps.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE

COPY example-stubs /$WM_PACKAGE/example-stubs
COPY bilateral_stubs /$WM_PACKAGE/bilateral_stubs
COPY avengers-stubs /$WM_PACKAGE/avengers-stubs
COPY marvel-stubs /$WM_PACKAGE/marvel-stubs

COPY orion-stubs /$WM_PACKAGE/orion-stubs
COPY stubs/mappings/infra /$WM_PACKAGE/orion-stubs/mappings
COPY stubs/__files/infra /$WM_PACKAGE/orion-stubs/__files/infra

COPY fiberbau-stubs /$WM_PACKAGE/fiberbau-stubs
COPY order-stubs /$WM_PACKAGE/order-stubs
COPY osr-stubs /$WM_PACKAGE/osr-stubs
COPY presales-stubs /$WM_PACKAGE/presales-stubs
COPY merlin-performance-stubs /$WM_PACKAGE/merlin-performance-stubs
COPY gteam-stubs/__files/ /$WM_PACKAGE/__files/gteam-stubs
COPY gteam-stubs/mappings/ /$WM_PACKAGE/mappings/gteam-stubs

COPY pocemongo-stubs /$WM_PACKAGE/pocemongo-stubs

COPY tmi-stubs /$WM_PACKAGE/tmi-stubs

COPY schulung-stubs /$WM_PACKAGE/schulung-stubs

COPY ntoa-stubs /$WM_PACKAGE/ntoa-stubs

EXPOSE 8080

CMD java -jar wiremock-acc-deps.jar --max-request-journal-entries $MAX_REQUEST_JOURNAL_ENTRIES --disable-gzip --async-response-enabled true --extensions com.tsystems.tm.acc.wiremock.groovy.GroovyPostServeAction,com.tsystems.tm.acc.wiremock.webhook.WebhookPostServeAction,com.tsystems.tm.acc.wiremock.webhook.WebhooksPostServeAction,com.tsystems.tm.acc.wiremock.persist.PersistencePostServeAction,com.tsystems.tm.acc.wiremock.persist.PersistenceArrayPostServeAction,com.tsystems.tm.acc.wiremock.persist.endpoint.PersistenceAdminApi,com.tsystems.tm.acc.wiremock.CustomHelpersResponseTemplateTransformer,com.tsystems.tm.acc.wiremock.groovy.GroovyRequestMatcherExtension --root-dir $STUBS_PATH --container-threads $CONTAINER_THREADS --async-response-threads $ASYNC_RESPONSE_THREADS $ADDITIONAL_OPTIONS
