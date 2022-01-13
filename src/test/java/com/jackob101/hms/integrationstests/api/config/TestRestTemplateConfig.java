package com.jackob101.hms.integrationstests.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

@Lazy
@TestConfiguration
public class TestRestTemplateConfig {

    @Bean
    public TestRestTemplate testRestTemplate(@LocalServerPort int port) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port);

        TestRestTemplate testRestTemplate = new TestRestTemplate(restTemplateBuilder);

        testRestTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                }));

        return testRestTemplate;
    }
}
