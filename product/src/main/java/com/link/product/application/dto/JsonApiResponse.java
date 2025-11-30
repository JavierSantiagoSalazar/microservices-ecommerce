package com.link.product.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class JsonApiResponse<T> {

    private T data;
    private Map<String, String> links;
    private Map<String, Object> meta;

    public JsonApiResponse(T data) {
        this.data = data;
    }

}

