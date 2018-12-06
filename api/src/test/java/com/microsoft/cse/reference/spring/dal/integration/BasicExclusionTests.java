package com.microsoft.cse.reference.spring.dal.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = BasicExclusionTests.Config.class)
public class BasicExclusionTests {
    @Autowired
    TestRestTemplate rest;

    @LocalServerPort
    Integer httpPort;

    @Test
    public void ExcludeTitleRepository() {
        ResponseEntity<String> res = this.rest.getForEntity("http://localhost:" + httpPort + "/titles", String.class);
        Assert.assertTrue(res.getStatusCode().is4xxClientError());
    }

    /**
     * A configuration instance for these tests
     */
    public static class Config extends PropertyMockingApplicationContextInitializer {
        @Override
        protected String[] getExcludeList() {
            // we wish to disable the TitleRepository for the tests above, so we exclude them
            return new String[] { "TitleRepository" };
        }
    }
}
