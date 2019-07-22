package com.tsystems.tm.acc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.tomakehurst.wiremock.http.Body;

import java.io.IOException;

public class BodySerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o instanceof Body) {
            Body body = (Body) o;
            jsonGenerator.writeObject(body.isBinary() ? null : body.asString());

        } else {
            jsonGenerator.writeObject(o);
        }
    }
}
