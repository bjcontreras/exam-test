package com.javbre.config;

import com.javbre.utilities.Utilities;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private final OpenApiProperties openApiProperties;

    @Value("${springdoc.openapi.dev-url}")
    private String devUrl;

    @Autowired
    public OpenApiConfig(OpenApiProperties openApiProperties) {
        this.openApiProperties = openApiProperties;
    }

    @Bean
    public OpenAPI myOpenAPI() {

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription(this.openApiProperties.getProjectShortDescription());

        Contact contact = new Contact()
                .name(this.openApiProperties.getDeveloperName())
                .url(this.openApiProperties.getOrganizationUrl());

        License mitLicense = new License()
                .name("MIT License")
                .url(this.openApiProperties.getProjectLicenceLink());

        Info info = new Info()
                .title(this.openApiProperties.getProjectName())
                .version("1.0")
                .contact(contact)
                .description(this.openApiProperties.getProjectShortDescription())
                .termsOfService(this.openApiProperties.getProjectTosMsg())
                .license(mitLicense);

        Components components = new Components()
                .addSecuritySchemes("Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));

        return new OpenAPI()
                .components(components)
                .info(info)
                .servers(List.of(devServer));
    }

    @Bean
    public OperationCustomizer addGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addSecurityItem(new SecurityRequirement().addList("Authorization"));

            operation.addParametersItem(new Parameter()
                    .in("header")
                    .required(false)
                    .name("msgtype")
                    .description("Tipo de mensaje")
                    .schema(new Schema<String>().type("string"))
                    .example("Request"));

            operation.addParametersItem(new Parameter()
                    .in("header")
                    .required(false)
                    .name("timestamp")
                    .description("Marca de tiempo de la solicitud")
                    .schema(new Schema<String>().type("string"))
                    .example(Utilities.getTimestampValue()));

            return operation;
        };
    }

}
