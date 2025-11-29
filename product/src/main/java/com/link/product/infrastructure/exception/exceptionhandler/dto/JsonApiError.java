package com.link.product.infrastructure.exception.exceptionhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiError {
    private String status;
    private String title;
    private String detail;
    private String source;
}
