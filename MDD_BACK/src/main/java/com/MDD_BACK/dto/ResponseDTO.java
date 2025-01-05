package com.MDD_BACK.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseDTO {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public ResponseDTO() {}

    public ResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}