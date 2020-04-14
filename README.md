# Central Wiremock Repository

Groovy Helper
---
Allows to use groovy to create response
You need to setup GROOVY_ROOT env variable to setup root path for groovy scripts (default: ./groovy)

Example:
```json
{
  // Use 'inline' property to set inline script
  // You can add any number of args, they will be available with given names.
  "simple": "{{groovy  arg1='request.body' arg2='request.headers' inline='context.get(arg1)'}}",
  // You can use this sintax to pass whichever is in between to variable 'inner' in script 
  "withInput": "{{#groovy arg1='request.body' arg2='request.headers' inline='context.get(arg2)'}}{{request.body}}{{/groovy}}",
   // Use 'scriptFilename' to set groovy script filename
  "file": "{{#groovy arg1='request.body' arg2='request.headers' scriptFilename='example.groovy'}}{{request.body}}{{/groovy}}",
  "vanilla": "{{jsonPath request.body '$.test'}}"
}
```

Webhook Extension
---
Allows to do callbacks

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
Example of for cycle
```json
[
  {{#each (jsonPath request.body '$.klsIds')}}
  {
    "klsId": {{this}}
  }{{#unless @last}},{{/unless}}
  {{/each}}
]
```
Example of for Environment variable
```json
"system": "{{systemValue type='ENVIRONMENT' key='TEST_VALUE'}}"
```
Example oauth helper.
```json
{
  "request": {
    "method": "POST",
    "url": "/test/auth"
  },
  "response": {
    "body": "Ok",
    "headers": {
      "Content-Type": "text/plain"
    },
    "status": 200
  },
  "postServeActions": {
    "webhook": {
      "url": "http://localhost:8080/api/genericCallbackConsumer/v1.0/consume/",
      "headers": {
        "Authorization": "Bearer {{oauth}}",
        "Content-Type": "application/json"
      },
      "method": "POST",
      "body": "OK"
    }
  }
}
```

Project structure
---
Stubs are in the folder "stubs"
- stubs
  - __files - storage for body contents
      - partner - storage for partner systems body contents. Shared between all teams
      - domain1 - storage for body contents for a specific domain env and team env in that domain
          - service1 - storage for body contents of this service, eg. if this service is not deployed what it should respond.
          - service2 - ...
      - domain2 - ...
  - mappings - storage for json definitions of stubs
      - partner - storage for partner systems stubs. Shared between all teams
      - domain1 - storage for stubs for a specific domain env and team env in that domain
          - service1 - storage for stubs of this service, eg. if this service is not deployed what it should respond.
          - service2 - ...
      - domain2 - ...
 
 Ideology
 ---
 As it is a central storage, it will contain all stubs for all envs and deployed to all envs.
 It contains only "generic" stubs. That means that stubs in this repository shall not be test case specific.
 They represent MVP of stubs to enable the environment to be operational.
 
 How to add new stubs
 * First check if there is already stub created for your endpoint
 * If not create it, use as much templating as possible
 * Use positive cases, negative ones are test case specific
 * Is stub already exists, check if you can reuse it.
 * If not either contact whoever created it to resolve conflict, or add a similar one


Example: Simple webhook receiver
---

Wiremock can be used as a simple webhook/callback receiver.

## Locally
* `docker run --rm -ti -p 8080:8080 mtr.external.otc.telekomcloud.com/digitalhub/wiremock-acc:develop`
* Send your callback to http://localhost:8080/api/genericCallbackConsumer/v1.0/consume/
* Find the callback
  * http://localhost:8080/__admin/swagger-ui/#/Requests/post___admin_requests_find
    * Request body: `{"method":"POST","url":"/api/genericCallbackConsumer/v1.0/consume/"}`
  * `curl -X POST "http://localhost:8080/__admin/requests/find" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"method\":\"POST\",\"url\":\"/api/genericCallbackConsumer/v1.0/consume/\"}"`
* More details: http://localhost:8080/__admin/docs

More
---
You can read more about creating stubs here: https://gard.telekom.de/gardwiki/display/DGHB/Wiremock+Stubs+Handling