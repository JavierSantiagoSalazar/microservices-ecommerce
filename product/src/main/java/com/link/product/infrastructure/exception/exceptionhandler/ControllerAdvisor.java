package com.link.product.infrastructure.configuration.exceptionhandler;

import com.link.product.domain.exceptions.ProductAlreadyExistsException;
import com.pragma.emazon_stock.domain.exceptions.ProductAlreadyExistsException;
import com.pragma.emazon_stock.domain.exceptions.ProductCategoryOutOfBoundsException;
import com.pragma.emazon_stock.domain.exceptions.ProductNotFoundException;
import com.pragma.emazon_stock.domain.exceptions.BrandAlreadyExistsException;
import com.pragma.emazon_stock.domain.exceptions.BrandDoesNotExistException;
import com.pragma.emazon_stock.domain.exceptions.CategoryAlreadyExistsException;
import com.pragma.emazon_stock.domain.exceptions.CategoryDoesNotExistException;
import com.pragma.emazon_stock.domain.exceptions.InvalidFilteringParameterException;
import com.pragma.emazon_stock.domain.exceptions.JwtIsEmptyException;
import com.pragma.emazon_stock.domain.exceptions.NoContentProductException;
import com.pragma.emazon_stock.domain.exceptions.NoContentBrandException;
import com.pragma.emazon_stock.domain.exceptions.NoContentCategoryException;
import com.pragma.emazon_stock.domain.exceptions.NotUniqueProductCategoriesException;
import com.pragma.emazon_stock.domain.exceptions.PageOutOfBoundsException;
import com.pragma.emazon_stock.domain.exceptions.SupplyAmountMismatchException;
import com.pragma.emazon_stock.domain.exceptions.TransactionServiceUnavailableException;
import com.pragma.emazon_stock.domain.exceptions.UnauthorizedException;
import com.pragma.emazon_stock.domain.utils.Constants;
import com.pragma.emazon_stock.infrastructure.configuration.exception.dto.PaginatedResponse;
import com.pragma.emazon_stock.infrastructure.configuration.exception.dto.Response;
import feign.RetryableException;
import org.apache.coyote.BadRequestException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.util.List;

@ControllerAdvice
public class ControllerAdvisor {

   
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Response> handleProductAlreadyExistsException(
            ProductAlreadyExistsException productAlreadyExistsException
    ) {
        return new ResponseEntity<>(
                Response.builder()
                        .statusCode(HttpStatus.CONFLICT)
                        .message(Constants.ARTICLE_ALREADY_EXISTS_EXCEPTION_MESSAGE)
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Response> handleProductNotFoundException(
            ProductNotFoundException productNotFoundException
    ) {
        return new ResponseEntity<>(
                Response.builder()
                        .statusCode(HttpStatus.NOT_FOUND)
                        .message(productNotFoundException.getMessage() + productNotFoundException.getNotFoundIds())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(NoContentProductException.class)
    public ResponseEntity<Response> handleNoContentProductException(
            NoContentProductException noContentProductException
    ) {
        return new ResponseEntity<>(
                Response.builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .message(Constants.ARTICLE_NO_CONTENT_MESSAGE)
                        .build(),
                HttpStatus.NO_CONTENT
        );
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Response> handleConnectException(
            ConnectException connectException
    ) {
        return new ResponseEntity<>(
                Response.builder()
                        .statusCode(HttpStatus.SERVICE_UNAVAILABLE)
                        .message(connectException.getMessage())
                        .build(),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleBadRequestException(
            BadRequestException badRequestException
    ) {
        return new ResponseEntity<>(
                Response.builder()
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .message(badRequestException.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

}
