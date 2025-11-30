package com.link.inventory.infrastructure.exception.exceptionhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiErrorResponse {

    private List<JsonApiError> errors;

    public JsonApiErrorResponse(JsonApiError error) {
        this.errors = List.of(error);
    }

}
