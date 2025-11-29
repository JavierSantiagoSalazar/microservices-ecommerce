package com.pragma.emazon_stock.infrastructure.input.rest;

import com.pragma.emazon_stock.application.dto.article.ArticleRequest;
import com.pragma.emazon_stock.application.dto.article.ArticleResponse;
import com.pragma.emazon_stock.application.dto.article.SupplyRequest;
import com.pragma.emazon_stock.application.handler.article.ArticleHandler;
import com.pragma.emazon_stock.domain.model.Pagination;
import com.pragma.emazon_stock.domain.utils.Constants;
import com.pragma.emazon_stock.domain.utils.HttpStatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleRestController {

    @Value("${article.name.path}")
    private String articleNamePath;

    private final ArticleHandler articleHandler;

    @Operation(summary = Constants.ARTICLE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.CREATED,
                    description = Constants.ARTICLE_CREATED,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.CONFLICT,
                    description = Constants.ARTICLE_ALREADY_EXISTS,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.ARTICLE_DUPLICATE_CATEGORY_NAMES,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.ARTICLE_CATEGORY_LIMIT,
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<Void> createArticle(@Valid @RequestBody ArticleRequest articleRequest) {

        articleHandler.createArticle(articleRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(articleNamePath)
                .buildAndExpand(articleRequest.getArticleName())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = Constants.ARTICLE_GET_ALL_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ARTICLE_OBTAINED,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ArticleResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.NO_CONTENT,
                    description = Constants.ARTICLE_NO_CONTENT,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.ARTICLE_INVALID_FILTER,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.ARTICLE_PAGE_OUT_OF_RANGE,
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<Pagination<ArticleResponse>> getArticles(
            @RequestParam(defaultValue = Constants.ASC_SORT_ORDER) String sortOrder,
            @RequestParam(defaultValue = Constants.ARTICLE_NAME) String filterBy,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = Constants.SIZE_DEFAULT_VALUE) Integer size
    ) {
        return ResponseEntity.ok(articleHandler.getArticles(sortOrder, filterBy, brandName, categoryName, page, size));
    }

    @Operation(summary = Constants.ARTICLE_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ARTICLE_UPDATED,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.NOT_FOUND,
                    description = Constants.ARTICLE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.ARTICLE_INVALID_REQUEST,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.SUPPLY_MISMATCH,
                    content = @Content
            )
    })
    @PatchMapping("/")
    public ResponseEntity<Boolean> updateArticleAmount(@Valid @RequestBody SupplyRequest supplyRequest) {
        boolean isUpdated = articleHandler.updateArticleSupply(supplyRequest);
        return ResponseEntity.ok(isUpdated);
    }

    @Operation(summary = Constants.GET_ARTICLES_BY_IDS)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ARTICLES_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.NOT_FOUND,
                    description = Constants.ARTICLE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.INVALID_REQUEST,
                    content = @Content
            )
    })
    @GetMapping("/get-articles-by-ids")
    public ResponseEntity<List<ArticleResponse>> getArticlesByIds(@RequestParam List<Integer> articleIdList) {
        List<ArticleResponse> responseList = articleHandler.getArticlesByIds(articleIdList);
        return ResponseEntity.ok(responseList);
    }

}
