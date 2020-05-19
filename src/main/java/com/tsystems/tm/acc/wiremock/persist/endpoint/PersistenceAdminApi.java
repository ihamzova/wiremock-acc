package com.tsystems.tm.acc.wiremock.persist.endpoint;

import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.tsystems.tm.acc.wiremock.persist.endpoint.tasks.*;

public class PersistenceAdminApi implements AdminApiExtension {

    @Override
    public void contributeAdminApiRoutes(Router router) {
        router.add(RequestMethod.GET, "/persistence", GetPersistenceContentsTask.class);
        router.add(RequestMethod.GET, "/persistence/{name}", GetPersistenceDataValueTask.class);
        router.add(RequestMethod.POST, "/persistence", AddPersistenceDataTask.class);
        router.add(RequestMethod.PUT, "/persistence", ModifyPersistenceDataTask.class);
        router.add(RequestMethod.DELETE, "/persistence", ClearPersistenceTask.class);
        router.add(RequestMethod.DELETE, "/persistence/{name}", DeletePersistenceDataTask.class);
    }

    @Override
    public String getName() {
        return "Persistence";
    }
}
