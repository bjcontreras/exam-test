package com.javbre.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javbre.utilities.Utilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HeaderValidationFilter extends OncePerRequestFilter {

    private static final Set<String> REQUIRED_HEADERS =
            Set.of("authorization", "system", "operation", "timestamp", "msgtype");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Enumeration<String> headerNames = request.getHeaderNames();
        String validationMessage = validateHeadersContent(headerNames);

        if (validationMessage != null) {
            // Preparar respuesta JSON estándar
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            LinkedHashMap<String, String> body = new LinkedHashMap<>();
            body.put("timestamp", Utilities.getTimestampValue());
            body.put("error", validationMessage);
            body.put("path", request.getRequestURI());

            String json = objectMapper.writeValueAsString(body);
            response.getWriter().write(json);
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String validateHeadersContent(Enumeration<String> headerNames) {
        if (headerNames == null) {
            return "Se deben enviar los headers requeridos.";
        }

        Set<String> headersPresent = Collections.list(headerNames).stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toSet());

        for (String required : REQUIRED_HEADERS) {
            if (!headersPresent.contains(required)) {
                return "El header " + required + " es requerido.";
            }
        }
        return null;
    }
}