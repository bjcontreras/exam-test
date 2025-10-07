package com.javbre.config;

//import com.javbre.config.filter.CorsFilter;
import com.javbre.config.filter.CorsFilter;
import com.javbre.config.filter.CspNonceFilter;
import com.javbre.config.filter.SecurityHeadersFilter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityServiceConfig implements WebMvcConfigurer {

    @Value("${controller.properties.base-path}")
    private String urlBase;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        // HSTS
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true)
                                .preload(true)
                        )
                        // Clickjacking
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        // X-Content-Type-Options: nosniff
                        .contentTypeOptions(Customizer.withDefaults())
                        // Referrer-Policy
                        .referrerPolicy(ref -> ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests

                        // SWAGGER
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // METRIC
                        .requestMatchers("/actuator/**").permitAll()

                        // ERROR
                        .requestMatchers("/error").permitAll()

                        // MICRO
                        .requestMatchers(HttpMethod.POST, urlBase + "/exams").permitAll()
                        .requestMatchers(HttpMethod.POST, urlBase + "/exams/**").permitAll()
                        .requestMatchers(HttpMethod.GET, urlBase + "/exams/**").permitAll()
                        .requestMatchers(HttpMethod.POST, urlBase + "/students").permitAll()
                        .requestMatchers(HttpMethod.POST, urlBase + "/student-exams/**").permitAll()
                        .requestMatchers(HttpMethod.GET, urlBase + "/student-exams/**").permitAll()
                        .requestMatchers(HttpMethod.POST, urlBase).permitAll()
                );

        return http.build();
    }

    // Registra el filtro de nonce CSP con prioridad alta
    @Bean
    public FilterRegistrationBean<CspNonceFilter> cspNonceFilterRegistration(CspNonceFilter filter) {
        FilterRegistrationBean<CspNonceFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return reg;
    }

    // Registra el filtro de cabeceras estáticas
    @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilterRegistration(SecurityHeadersFilter filter) {
        FilterRegistrationBean<SecurityHeadersFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);
        return reg;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> factory.addContextCustomizers(context ->
                context.setUseHttpOnly(true)
        );
    }

}
