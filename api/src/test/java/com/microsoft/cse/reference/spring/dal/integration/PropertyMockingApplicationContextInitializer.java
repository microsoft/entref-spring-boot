package com.microsoft.cse.reference.spring.dal.integration;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.config.Net;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;

import java.io.IOException;

/**
 * Allows mocking of application context properties
 */
public abstract class PropertyMockingApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    /**
     * Get the mongo data repository exclusion list
     * @apiNote see Constants.ENV_EXCLUDE_FILTER for more info
     * @return the mongo data repository exclusion list
     */
    protected String[] getExcludeList() {
        return new String[0];
    }

    /**
     * Get the CORS allowed origin value
     * @return the CORS allowed origin value
     */
    protected String getAllowedOrigin() { return ""; }


    /**
     * Get the net binding information for the embedded mongo server
     * @return net binding information
     */
    protected Net getMongoNet() {
        try {
            return new Net();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get the database name
     * @return the database name
     */
    protected String getDbName() {
        return "test";
    }

    /**
     * Get the embedded mongo server instance
     * @param bind the net binding information to bind to
     * @return the embedded mongo server instance
     */
    protected MongodExecutable getMongo(Net bind) {
        try {
            return Helpers.SetupMongo(bind);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get the rest template builder with which to test rest endpoints
     * @return the rest template builder
     */
    protected RestTemplateBuilder getRestTemplateBuilder() {
        return new RestTemplateBuilder().setConnectTimeout(1000).setReadTimeout(1000);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // configure a net binding instance
        Net mongoNet = this.getMongoNet();

        // register some autowire-able dependencies, to make leveraging the configured instances in a test possible
        applicationContext.getBeanFactory().registerResolvableDependency(RestTemplateBuilder.class, this.getRestTemplateBuilder());
        applicationContext.getBeanFactory().registerResolvableDependency(Net.class, mongoNet);
        applicationContext.getBeanFactory().registerResolvableDependency(MongodExecutable.class, this.getMongo(mongoNet));

        // configure the property sources that will be used by the application
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        MockPropertySource mockEnvVars = new MockPropertySource()
                .withProperty(Constants.ENV_DB_NAME, this.getDbName())
                .withProperty(Constants.ENV_DB_CONNSTR, "mongodb://localhost:" + mongoNet.getPort())
                .withProperty(Constants.ENV_ALLOWED_ORIGIN, this.getAllowedOrigin())
                .withProperty(Constants.ENV_EXCLUDE_FILTER, String.join(",", this.getExcludeList()));

        // inject the property sources into the application as environment variables
        propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mockEnvVars);
    }
}