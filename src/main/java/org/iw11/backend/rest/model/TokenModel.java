package org.iw11.backend.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenModel {

    @JsonProperty("token")
    private String token;

    public TokenModel() { }

    public TokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
