package com.link.product.infrastructure.configuration.security;

import com.link.product.domain.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${app.api.key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String apiKey = extractApiKey(request);

        if (!validApiKey.equals(apiKey)) {

            log.warn(Constants.INVALID_API_KEY_LOG, request.getRemoteAddr());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(Constants.JSON_API_MEDIA_TYPE);
            response.getWriter().write(String.format("""
                {
                    "errors": [{
                        "status": "%s",
                        "title": "%s",
                        "detail": "%s"
                    }]
                }
                """,
                    Constants.STATUS_401,
                    Constants.UNAUTHORIZED_TITLE,
                    Constants.INVALID_API_KEY_DETAIL
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader(Constants.HEADER_X_API_KEY);
        if (apiKey != null) return apiKey;

        String authHeader = request.getHeader(Constants.HEADER_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constants.API_KEY_PREFIX)) {
            return authHeader.substring(Constants.API_KEY_PREFIX.length());
        }

        return null;
    }

}