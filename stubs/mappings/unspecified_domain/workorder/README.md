# Workorder mocks

### Enabling workorder mocks for mobile-app

In team environment configuration repository the are *.yaml* files for container configuration of services. 
As example we use gandalf-02 environment.
```http request
https://digihub-wbench.wesp.telekom.net/gitlab/infr/deployment/environments/team/gigabit-tm-gandalf-02
```

As calls to *workorder* service from *mobile-workorder* app goes through *mobile-workorder-bff* service, we have to enable mocking on this service.
For that we edit *mobile-workorder-bff.yaml* file:
```http request
https://digihub-wbench.wesp.telekom.net/gitlab/infr/deployment/environments/team/gigabit-tm-gandalf-02/-/blob/master/mobile-workorder-bff.yaml
```
and add line:
```yaml
REST_API_WORKORDER_APP_URL: "http://wiremock-acc-app/"
```

so that resulting file would be: 

```yaml
app:
    env:
        # public keycloak
        EXT_RHSSO_HOST: "https://portal-proxy-gandalf-02.magic-dev.telekom.de/auth/"
        RHSSO_REALM: "LeonardWeiss"

        # wiremock redirects
        REST_API_GFAP_MATERIAL_CATALOG_APP_URL: "http://wiremock-gandalf-app/"
        REST_API_WORKORDER_APP_URL: "http://wiremock-acc-app/"
```

### Technician task details (mobile-workorder-app view)
As parameter in URL we have to specify *klsId* from existing exploration protocol:
```http request
https://portal-proxy-<env>.magic-dev.telekom.de/auftragnehmerportal-mui/?a-cid=47100#/exploration/<klsId>/edit/
```

### Scenario for exploration completion and process logs

Click "Auskundung abschließen" button to complete exploration.

##### Successful exploration and full process log
Successful exploration is a standard behaviour.
Reset scenario to *Started* state to perfrom successful exploration once again:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-<env>.support.magic.telekom.de/reset/exploration-workorder-completion/started
```

##### Unsuccessful exploration and fibre on location update failure in process log
Reset scenario to *before-fol-error* state to perfrom this option of unsuccessful exploration:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-<env>.support.magic.telekom.de/reset/exploration-workorder-completion/before-fol-error
```

##### Unsuccessful exploration and pdf creation failure in process log
Reset scenario to *before-pdf-error* state to perfrom this option of unsuccessful exploration:
```http request
(POST) https://giga:bit@wiremock-acc-proxy-<env>.support.magic.telekom.de/reset/exploration-workorder-completion/before-pdf-error
```
