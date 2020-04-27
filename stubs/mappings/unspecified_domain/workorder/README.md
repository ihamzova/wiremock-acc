# Workorder mocks

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
