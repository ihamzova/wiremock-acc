package com.tsystems.tm.acc.wiremock.persist.endpoint.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.admin.Paginator;
import com.github.tomakehurst.wiremock.admin.model.PaginatedResult;
import com.tsystems.tm.acc.wiremock.persist.endpoint.model.data.PersistencePair;

import java.util.List;

public class PersistenceResult extends PaginatedResult<PersistencePair> {

    @JsonCreator
    public PersistenceResult(@JsonProperty("values") List<PersistencePair> values,
                                  @JsonProperty("meta") Meta meta) {
        super(values, meta);
    }

    public PersistenceResult(Paginator<PersistencePair> paginator) {
        super(paginator);
    }

    public List<PersistencePair> getValues() {
        return select();
    }
}
