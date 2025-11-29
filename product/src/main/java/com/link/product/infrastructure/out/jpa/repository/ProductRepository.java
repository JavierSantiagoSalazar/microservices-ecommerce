package com.pragma.emazon_stock.infrastructure.out.jpa.repository;

import com.pragma.emazon_stock.infrastructure.out.jpa.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

    Optional<ArticleEntity> findByArticleName(String articleName);

    @Query("SELECT a FROM ArticleEntity a WHERE a.articleId IN (:articleIds)")
    List<ArticleEntity> findAllByArticleId(@Param("articleIds") List<Integer> articleIds);

}
