package com.tsystems.tm.acc.wiremock.persist.endpoint.tasks;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static java.net.HttpURLConnection.*;

public class DeletePersistenceDataTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String name = pathParams.get("name");

        if (PersistenceService.get().get(name) == null) {
            return ResponseDefinitionBuilder.responseDefinition()
                    .withStatus(HTTP_NOT_FOUND)
                    .withHeader(CONTENT_TYPE, "application/json")
                    .withBody(Json.write("Key " + name + " does not exist"))
                    .build();
        }

        PersistenceService.get().clear(name);
        return new ResponseDefinitionBuilder().withStatus(HTTP_NO_CONTENT).
                withHeader("Content-Type", "application/json").build();
    }
}
