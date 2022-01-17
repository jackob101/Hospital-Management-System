package com.jackob101.hms.integrationstests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class TestUtils {

    ObjectMapper objectMapper;

    String baseRequestMapping;

    TestRestTemplate testRestTemplate;


    public TestUtils(String baseRequestMapping, TestRestTemplate testRestTemplate) {
        this.baseRequestMapping = baseRequestMapping;
        this.testRestTemplate = testRestTemplate;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <Type> ResponseEntity<Type> createEntity(Object entity, Class<Type> responseType) throws JsonProcessingException {

        String content = objectMapper.writeValueAsString(entity);

        return testRestTemplate.postForEntity(baseRequestMapping,
                content,
                responseType);

    }

    public <Type> ResponseEntity<Type> updateEntity(Object entity, Class<Type> responseType) {

        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(entity);

        return testRestTemplate.exchange(baseRequestMapping,
                HttpMethod.PUT,
                objectHttpEntity,
                responseType);
    }

    public <Type> ResponseEntity<Type> findEntity(Long id, Class<Type> responseType) {

        return testRestTemplate
                .getForEntity(baseRequestMapping + "/" + id, responseType);

    }

    public <Type> ResponseEntity<Type> deleteEntity(Long id, Class<Type> responseType) {

        return testRestTemplate.exchange(baseRequestMapping + "/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                responseType);
    }

    public <Type> ResponseEntity<Type> findAll(Class<Type> responseType) {

        return testRestTemplate.getForEntity(baseRequestMapping + "/all", responseType);
    }

}
