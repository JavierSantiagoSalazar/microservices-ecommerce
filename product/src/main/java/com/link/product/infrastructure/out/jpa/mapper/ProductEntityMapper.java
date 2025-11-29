package com.link.product.infrastructure.out.mapper;

import com.pragma.emazon_stock.domain.model.Article;
import com.pragma.emazon_stock.domain.utils.Constants;
import com.pragma.emazon_stock.infrastructure.out.jpa.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL)
public interface ProductEntityMapper {

    ArticleEntity toEntity(Article article);

    @Mapping(target = "articleBrand.brandArticles", ignore = true)
    Article toDomain(ArticleEntity articleEntity);

    @Mapping(target = "articleBrand.brandArticles", ignore = true)
    List<Article> toDomainList(List<ArticleEntity> articleEntityList);

    List<ArticleEntity> toEntityList(List<Article> articles);

}
