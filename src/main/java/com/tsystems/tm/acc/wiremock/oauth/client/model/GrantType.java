/*
 * rhsso
 * rhsso
 *
 * The version of the OpenAPI document: 1.0.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.tsystems.tm.acc.wiremock.oauth.client.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gets or Sets GrantType
 */
@JsonAdapter(GrantType.Adapter.class)
public enum GrantType {

    CLIENT_CREDENTIALS("client_credentials"),

    AUTHORIZATION_CODE("authorization_code"),

    PASSWORD("password");

    private String value;

    GrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static GrantType fromValue(String value) {
        for (GrantType b : GrantType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<GrantType> {
        @Override
        public void write(final JsonWriter jsonWriter, final GrantType enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public GrantType read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return GrantType.fromValue(value);
        }
    }
}

