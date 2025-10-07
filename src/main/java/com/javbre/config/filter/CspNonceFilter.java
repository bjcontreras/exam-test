package com.javbre.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CspNonceFilter extends OncePerRequestFilter {

    public static final String CSP_NONCE_ATTR = "cspNonce";

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String nonce = Base64.getEncoder().encodeToString(
                SecureRandom.getInstanceStrong().generateSeed(16));
        request.setAttribute(CSP_NONCE_ATTR, nonce);

        String csp = "default-src 'self'; " +
                "script-src 'self' 'nonce-" + nonce + "'; " +
                "style-src 'self' 'nonce-" + nonce + "'; " +
                "object-src 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self'; " +
                "img-src 'self' data:; " +
                "font-src 'self'; " +
                "connect-src 'self'; " +
                "frame-ancestors 'none'; " +
                "upgrade-insecure-requests; ";

        response.setHeader("Content-Security-Policy", csp);
        filterChain.doFilter(request, response);
    }
}

