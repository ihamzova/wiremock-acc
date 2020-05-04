# Workorder mocks

### Enabling workorder mocks for mobile-exploration

In team environment configuration repository there are *.yaml* files for container configuration of services. 
As example we use tatra-01 environment.
```http request
https://digihub-wbench.wesp.telekom.net/gitlab/infr/deployment/environments/team/gigabit-tm-tatra-01
```

As calls to *workorder* service from *mobile-exploration* app goes through *mobile-exploration-bff* service, we have to enable mocking on this service.
For that we edit *mobile-exploration-bff.yaml* file:
```http request
https://digihub-wbench.wesp.telekom.net/gitlab/infr/deployment/environments/team/gigabit-tm-tatra-01/-/blob/master/mobile-exploration-bff.yaml
```
and add line:
```yaml
REST_API_WORKORDER_APP_URL: "http://wiremock-acc-app/"
```

so that resulting file would be: 

```yaml
app:
    env:
        REST_API_WORKORDER_APP_URL: "http://wiremock-acc-app/"
```

### Technician task details (mobile-exploration-app view)
As parameter in URL we have to specify *klsId* from existing exploration protocol (for example 123328):
```http request
https://portal-proxy-tatra-01.magic-dev.telekom.de/auftragnehmerportal-mui/exploration?a-cid=47100#/exploration/123328/edit/
```

### Scenario for exploration completion and process logs

Click "Auskundung abschließen" button to complete exploration.

##### Successful exploration and full process log
Successful exploration is a standard behaviour.
Reset scenario to *Started* state to perfrom successful exploration once again:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-tatra-01.support.magic.telekom.de/reset/exploration-workorder-completion/started
```

##### Unsuccessful exploration and fibre on location update failure in process log
Reset scenario to *before-fol-error* state to perfrom this option of unsuccessful exploration:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-tatra-01.support.magic.telekom.de/reset/exploration-workorder-completion/before-fol-error
```

##### Unsuccessful exploration and pdf creation failure in process log
Reset scenario to *before-pdf-error* state to perfrom this option of unsuccessful exploration:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-tatra-01.support.magic.telekom.de/reset/exploration-workorder-completion/before-pdf-error
```
