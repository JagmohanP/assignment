package com.assignment.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.assignment.exception.RestTemplateResponseErrorHandler;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(true).parameterName("mediaType")
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        final RestTemplate restTemplate = builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
        return restTemplate;
    }

    /**
     * Bean configuration for swagger UI
     *
     * @return bean with additional details to be shown on swagger UI
     */
    @Bean
    public OpenAPI countryServiceOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Country Service API")
                                .description("Application to provide various details of a country like code, capital, population")
                                .version("1.0-SNAPSHOT"))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("External site used to fetch country information")
                                .url("https://restcountries.com/"));
    }
}
