# wiremock-webhook-extension

Extension for wiremock.
Allows it to do async callbacks.
Based on https://github.com/wiremock/wiremock-webhooks-extension

* Code updated to java 1.8.
* Moved from gradle to maven.
* Added templating features.
* Added delayed callback feature

Example:
```json
{
  "request" : {
    "method" : "GET",
    "url" : "/async"
  },
  "response" : {
    "body" : "12377869",
    "headers" : {
      "Content-Type" : "application/json"
    },
    "status" : 200
  },
  "postServeActions": {
    "webhook": {
      "url": "{{request.headers.X-Callback-Url}}",
      "body": "IT WORKS",
      "method": "POST",
      "headers" : {
        "Content-Type" : "application/json"
      }
    }
  }
}
```