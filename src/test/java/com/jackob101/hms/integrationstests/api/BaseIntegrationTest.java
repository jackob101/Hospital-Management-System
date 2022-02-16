package com.jackob101.hms.integrationstests.api;

import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"no-security"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected TestUtils utils;

    protected String mapping;

    protected void configure(String mapping) {
        this.mapping = mapping;
        this.utils = new TestUtils(mapping, testRestTemplate);
    }
}
