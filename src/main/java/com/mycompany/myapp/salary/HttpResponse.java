package com.mycompany.myapp.salary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HttpResponse {

    @JsonProperty
    private String responseDesc;

    public String getResponseDesc() {
        return responseDesc;
    }

    public HttpResponse setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
        return this;
    }

    @Override
    public String toString() {
        return "HttpResponse{" + "responseDesc='" + responseDesc + '\'' + '}';
    }
}
