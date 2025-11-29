package com.pragma.emazon_stock.domain.spi;

import com.pragma.emazon_stock.domain.model.Article;

import java.util.List;

public interface ArticlePersistencePort {

    Article saveArticle(Article article);

    Boolean checkIfArticleExists(String articleName);

    List<Article> getArticlesByIds(List<Integer> articleIds);

    List<Article> getAllArticles();

    Boolean saveAllArticles(List<Article> articles);

}
