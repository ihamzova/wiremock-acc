package com.tsystems.tm.acc.wiremock.persist.endpoint.tasks;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;


public class ClearPersistenceTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        PersistenceService.get().clear();
        return new ResponseDefinitionBuilder().withStatus(HTTP_NO_CONTENT).
                withHeader("Content-Type", "application/json").build();
    }
}
