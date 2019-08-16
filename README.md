# Central Wiremock Repository

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

More
---
You can read more about creating stubs here: https://gard.telekom.de/gardwiki/display/DGHB/Wiremock+Stubs+Handling