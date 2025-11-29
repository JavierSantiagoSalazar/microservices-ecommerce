package com.link.product.domain.utils;

import lombok.Getter;

import static com.link.product.domain.utils.Constants.UTILITY_CLASS;

@Getter
public class HttpStatusCodes {

    private HttpStatusCodes() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String OK = "200";
    public static final String CREATED = "201";
    public static final String NO_CONTENT = "204";
    public static final String NOT_FOUND = "404";
    public static final String CONFLICT = "409";
    public static final String BAD_REQUEST = "400";

}
