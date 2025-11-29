package com.link.product.application.mappers;

import com.pragma.emazon_stock.application.dto.article.ArticleRequest;
import com.pragma.emazon_stock.domain.model.Article;
import com.pragma.emazon_stock.domain.model.Brand;
import com.pragma.emazon_stock.domain.model.Category;
import com.pragma.emazon_stock.domain.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleRequestMapper {

    @Mapping(source = "articleBrand", target = "articleBrand", qualifiedByName = "mapBrand")
    @Mapping(source = "articleCategories", target = "articleCategories", qualifiedByName = "mapCategories")
    Article toDomain(ArticleRequest articleRequest);

    @Named("mapBrand")
    default Brand mapBrand(String articleBrand) {
        return new Brand(null, articleBrand, null, null);
    }

    @Named("mapCategories")
    default List<Category> mapCategories(List<String> articleCategories) {

        return articleCategories.stream()
                .map(name -> new Category(null, name, null))
                .toList();
    }

}
