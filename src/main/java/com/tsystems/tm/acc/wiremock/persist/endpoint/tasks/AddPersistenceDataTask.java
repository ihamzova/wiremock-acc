package com.tsystems.tm.acc.wiremock.persist.endpoint.tasks;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.data.PersistencePair;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;

public class AddPersistenceDataTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        PersistencePair pair = Json.read(request.getBodyAsString(), PersistencePair.class);

        if (PersistenceService.get().get(pair.getName()) != null) {
            return ResponseDefinitionBuilder.responseDefinition()
                    .withStatus(HTTP_CONFLICT)
                    .withHeader(CONTENT_TYPE, "application/json")
                    .withBody(Json.write("Key " + pair.getName() + " already exists"))
                    .build();
        }

        PersistenceService.get().put(pair.getName(), pair.getValue());
        return ResponseDefinitionBuilder.jsonResponse(pair, HTTP_CREATED);
    }
}
