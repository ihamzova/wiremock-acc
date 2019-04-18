FROM mtr.external.otc.telekomcloud.com/digitalhub/wiremock:master

COPY target/wiremock-webhook-extension.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE

ENTRYPOINT ["java","-jar","wiremock-webhook-extension.jar"]
CMD ["--local-response-templating", "--extensions", "com.tsystems.tm.acc.Webhooks"]