package com.pragma.emazon_stock.domain.exceptions;

import com.pragma.emazon_stock.domain.utils.Constants;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleNotFoundException extends RuntimeException {

    private final List<Integer> notFoundIds;

    public ArticleNotFoundException(List<Integer> notFoundIds) {
        super(Constants.ARTICLE_DOES_NOT_EXIST);
        this.notFoundIds = notFoundIds;
    }

}
