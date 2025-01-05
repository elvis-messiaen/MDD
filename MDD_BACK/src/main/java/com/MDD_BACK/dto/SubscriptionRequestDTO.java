package com.MDD_BACK.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionRequestDTO {
    @JsonProperty("utilisateurId")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}