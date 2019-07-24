FROM mtr.external.otc.telekomcloud.com/digitalhub/wiremock:master

COPY target/wiremock-acc.jar /$WM_PACKAGE
COPY stubs /$WM_PACKAGE

ENTRYPOINT ["java","-jar","wiremock-acc.jar"]
CMD ["--disable-gzip", "--global-response-templating", "--extensions", "com.tsystems.tm.acc.Webhooks"]
