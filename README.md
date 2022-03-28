# Central Wiremock Repository

Curl Helper
---
Allows to make simple GET requests to remote resources.
You can store the response in the local variable and extract some parts using `jsonPath`.

```json
Example:
{
  // You should pass url into the body of the curl tag.
  // You can easily format the url with additional variables.
  "complex": "{{#assign 'relationData'}}{{#curl}}http://host/remote-relations/{{request.query.relationId}}{{/curl}}{{/assign}}{{jsonPath relationData '$.relationships[0].id'}}"
}
```
In Groovy script you can also have access to request thru variable "context" and to persistence thru variable "persistence".

Groovy Helper
---
Allows to use groovy to create response.
You need to setup GROOVY_ROOT env variable to setup root path for groovy scripts. (default: ./groovy)

```json
Example:
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
In Groovy script you can also have access to request thru variable "context" and to persistence thru variable "persistence".

Groovy Post Serve Action
---

```json
Example:
{
  ...
  "postServeActions": {
    "groovy": {
      "inline": "script here",
      "scriptFileName": "relative path to GROOVY_ROOT", // mutually exclusive with inline
      "arguments": {
        "arg1": "val1",
        "arg2": "val2"
      }
    }
  },
  ...
}
```

```json
Or multiple:
{
  ...
  "postServeActions": {
    "groovyArray": {
      "content": [
        {
          "inline": "script here",
          "scriptFileName": "relative path to GROOVY_ROOT",
          // mutually exclusive with inline
          "arguments": {
            "arg1": "val1",
            "arg2": "val2"
          }
        }
      ]
    }
  }
  ...
}
```

Persistence Helper
---
Allows to get data from persistent key/value storage.
Currently only in-memory db.

```json
Example:
{
  // Use 'key' property to define key to get from storage
  "simple": "{{persist  key='storedKey'}}"
}
```

If you need "get with default" functionality you may use this piece of code:

```json
Example:
{
  {{eq (persist key='my_key') null yes='my_default_value' no=(persist key='my_key')}}
}
```

Persistence Post Serve Action
---

```json
Example:
{
  ...
  "postServeActions": {
    "persist": {
      "action": "action, one of {set, unset, clearAll}",
      "key": "key",
      "value": "value",
      "filename": "relative path to __files", // mutually exclusive with value
      "fixedDelayMilliseconds": 12345
    }
  },
  ...
}
```

```json
or multiple:
{
  ...
  "postServeActions": {
    "persistArray": {
      "content": [
        {
          "action": "action, one of {set, unset, clearAll}",
          "key": "key",
          "value": "value",
          "filename": "relative path to __files",
          // mutually exclusive with value
          "fixedDelayMilliseconds": 12345
        }
      ]
    }
  },
  ...
}
```

Persistence Admin Api
---
If com.tsystems.tm.acc.wiremock.persist.endpoint.PersistenceAdminApi is part of the extensions section of this Dockerfile, then one can use a REST client tool like Postman to read out and manipulate the data that was stored with the Persistence (Array) Post Serve Action. One can GET, PUT, POST or DELETE entries in a wiremock-acc instance.

```json
URL-Pattern: https://wiremock-acc-app-[ENVIRONMENT-NAME].[HOSTNAME]/__admin/persistence
GET /persistence: gets all the key-value pairs stored
GET /persistence/{name}: gets a specific value for a key
POST /persistence: adds a new key-value pair
PUT /persistence: modifies an existing key-value pair
DELETE /persistence: deletes all stored entries
DELETE /persistence/{name}: deletes a specific entry for a key
```

```json
Json-Body-Example for a key-value (POST/PUT):
{
	"name": "WO.state.1234",
    "value": "IN_PROGRESS"
}
```

Webhook Extension
---
Allows to do callbacks.

```json
Example:
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

```json
Example of for cycle:
[
  {{#each (jsonPath request.body '$.klsIds')}}
  {
    "klsId": {{this}}
  }{{#unless @last}},{{/unless}}
  {{/each}}
]
```

```json
Example of for Environment variable:
"system": "{{unsafeSystemValue key='TEST_VALUE'}}"
```

```json
Example oauth helper:
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
**Stubs are in the folder "stubs"**
- stubs
  - **__files - storage for body contents**
      - partner - storage for partner systems body contents. Shared between all teams
      - domain1 - storage for body contents for a specific domain env and team env in that domain
          - service1 - storage for body contents of this service, eg. if this service is not deployed what it should respond.
          - service2 - ...
      - domain2 - ...
  - **mappings - storage for json definitions of stubs**
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
 
 **How to add new stubs:**
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
You can read more about creating stubs here: **https://gard.telekom.de/gardwiki/display/DGHB/Wiremock+Stubs+Handling**
