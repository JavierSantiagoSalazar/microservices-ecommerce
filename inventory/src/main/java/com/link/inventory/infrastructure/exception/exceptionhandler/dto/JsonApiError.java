package com.link.inventory.infrastructure.exception.exceptionhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonApiError {

    private String status;
    private String title;
    private String detail;
    private String source;

}
