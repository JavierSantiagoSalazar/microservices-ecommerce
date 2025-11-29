package com.pragma.emazon_stock.domain.usecase;

import com.pragma.emazon_stock.domain.api.ArticleServicePort;
import com.pragma.emazon_stock.domain.exceptions.ArticleAlreadyExistsException;
import com.pragma.emazon_stock.domain.exceptions.ArticleNotFoundException;
import com.pragma.emazon_stock.domain.exceptions.NoContentArticleException;
import com.pragma.emazon_stock.domain.exceptions.PageOutOfBoundsException;
import com.pragma.emazon_stock.domain.exceptions.SupplyAmountMismatchException;
import com.pragma.emazon_stock.domain.model.Article;
import com.pragma.emazon_stock.domain.model.Brand;
import com.pragma.emazon_stock.domain.model.Category;
import com.pragma.emazon_stock.domain.model.Pagination;
import com.pragma.emazon_stock.domain.model.Supply;
import com.pragma.emazon_stock.domain.model.SupplyTransaction;
import com.pragma.emazon_stock.domain.spi.ArticlePersistencePort;
import com.pragma.emazon_stock.domain.spi.BrandPersistencePort;
import com.pragma.emazon_stock.domain.spi.CategoryPersistencePort;
import com.pragma.emazon_stock.domain.spi.FeignClientPort;
import com.pragma.emazon_stock.domain.utils.ArticleValidator;
import com.pragma.emazon_stock.domain.utils.Constants;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.pragma.emazon_stock.domain.utils.Constants.DESC_COMPARATOR;
import static com.pragma.emazon_stock.domain.utils.Constants.GET_BY_BRAND;
import static com.pragma.emazon_stock.domain.utils.Constants.GET_BY_CATEGORY;
import static com.pragma.emazon_stock.domain.utils.Constants.PAGE_INDEX_HELPER;

@AllArgsConstructor
public class ArticleUseCase implements ArticleServicePort {


    private final ArticlePersistencePort articlePersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;
    private final BrandPersistencePort brandPersistencePort;
    private final FeignClientPort feignClientPort;

    @Override
    public void saveArticle(Article article) {

        if (checkIfArticleExists(article.getArticleName())) {
            throw new ArticleAlreadyExistsException();
        }

        List<String> categoryNames = article.getArticleCategories().stream()
                .map(Category::getName)
                .toList();

        ArticleValidator.validateCategoryNames(categoryNames);

        Brand brand = brandPersistencePort.getBrandByName(article.getArticleBrand().getBrandName());
        List<Category> categoryList = categoryPersistencePort.getAllCategoriesByName(categoryNames);

        article.setArticleBrand(brand);
        article.setArticleCategories(categoryList);
        Article savedArticle = articlePersistencePort.saveArticle(article);
        feignClientPort.addNewRegisterFromStock(
                new SupplyTransaction(savedArticle.getArticleId(), savedArticle.getArticleAmount())
        );
    }

    @Override
    public boolean checkIfArticleExists(String articleName) {
        return articlePersistencePort.checkIfArticleExists(articleName);
    }

    @Override
    public List<Article> getArticlesByIds(List<Integer> articleIdList) {
        List<Article> articles = articlePersistencePort.getArticlesByIds(articleIdList);

        List<Integer> notFoundIds = articleIdList.stream()
                .filter(id -> !articles.stream().map(Article::getArticleId).toList().contains(id))
                .toList();

        if (!notFoundIds.isEmpty()) {
            throw new ArticleNotFoundException(notFoundIds);
        }

        return articles;
    }

    @Override
    public Pagination<Article> getArticles(
            String sortOrder,
            String filterBy,
            String brandName,
            String categoryName,
            Integer page,
            Integer size
    ) {
        List<Article> articleList = new ArrayList<>(articlePersistencePort.getAllArticles());

        ArticleValidator.validateData(filterBy, articleList);

        articleList.sort(getComparatorForSorting(sortOrder));

        articleList = applyFilters(filterBy, brandName, categoryName, articleList);

        Integer totalItems = articleList.size();
        Integer totalPages = (int) Math.ceil((double) totalItems / size);
        Integer fromIndex = Math.min((page - PAGE_INDEX_HELPER) * size, totalItems);
        Integer toIndex = Math.min(fromIndex + size, totalItems);
        Boolean isLastPage = page >= totalPages;

        if (page > totalPages || page < PAGE_INDEX_HELPER) {
            throw new PageOutOfBoundsException(page, totalPages);
        }

        List<Article> paginatedArticles = articleList.subList(fromIndex, toIndex);

        if (paginatedArticles.isEmpty()) {
            throw new NoContentArticleException();
        }

        return new Pagination<>(
                paginatedArticles,
                page,
                size,
                (long) totalItems,
                totalPages,
                isLastPage
        );
    }

    @Override
    public Boolean updateArticleSupply(Supply supply) {

        List<Article> articleList = articlePersistencePort.getArticlesByIds(supply.getArticleIds());

        List<Integer> notFoundIds = supply.getArticleIds().stream()
                .filter(id -> articleList.stream().noneMatch(article -> article.getArticleId().equals(id)))
                .toList();

        validateSupplyData(supply, articleList, notFoundIds);

        for (int i = Constants.ZERO; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Integer amountToAdd = supply.getAmounts().get(i);
            article.setArticleAmount(article.getArticleAmount() + amountToAdd);
        }

        return articlePersistencePort.saveAllArticles(articleList);
    }

    private void validateSupplyData(Supply supply, List<Article> articleList, List<Integer> notFoundIds) {
        if (articleList.size() != supply.getArticleIds().size()) {
            throw new ArticleNotFoundException(notFoundIds);
        }

        if (articleList.size() != supply.getAmounts().size()) {
            throw new SupplyAmountMismatchException();
        }
    }

    private List<Article> applyFilters(String filterBy, String brandName, String categoryName, List<Article> articleList) {

        if (filterBy.equalsIgnoreCase(GET_BY_CATEGORY)) {
            return articleList.stream()
                    .filter(article -> article.getArticleCategories().stream()
                            .anyMatch(category -> category.getName().equals(categoryName)))
                    .toList();
        }

        if (filterBy.equalsIgnoreCase(GET_BY_BRAND)) {
            return articleList.stream()
                    .filter(article -> article.getArticleBrand().getBrandName().equals(brandName))
                    .toList();
        }

        return articleList;
    }

    private Comparator<Article> getComparatorForSorting(String sortOrder) {
        return DESC_COMPARATOR.equalsIgnoreCase(sortOrder) ?
                Comparator.comparing(Article::getArticleName).reversed() :
                Comparator.comparing(Article::getArticleName);
    }

}
