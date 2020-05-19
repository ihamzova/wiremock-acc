package com.tsystems.tm.acc.wiremock.persist.endpoint.tasks;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.LimitAndOffsetPaginator;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.tsystems.tm.acc.wiremock.persist.PersistenceService;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.PersistenceResult;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.data.PersistencePair;

import java.util.List;
import java.util.stream.Collectors;

public class GetPersistenceContentsTask implements AdminTask {

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        List<PersistencePair> persistData = PersistenceService.get().getAll().stream().map(
                elem -> new PersistencePair(elem.getKey(), elem.getValue())
              ).collect(Collectors.toList());
        PersistenceResult result = new PersistenceResult(
                LimitAndOffsetPaginator.fromRequest(persistData, request));

        return ResponseDefinitionBuilder.jsonResponse(result);
    }
}
