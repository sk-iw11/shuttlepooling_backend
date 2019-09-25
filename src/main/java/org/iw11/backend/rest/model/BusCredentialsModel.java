package org.iw11.backend.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusCredentialsModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("password")
    private String password;

    public BusCredentialsModel() { }

    public BusCredentialsModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
