package com.microsoft.cse.reference.spring.dal.config;

import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.web.internal.WebRequestTrackingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Profile("production")
public class ProductionConfig extends WebSecurityConfigurerAdapter implements IApplicationConfig {
    @Autowired
    Environment env;

    Logger logger = LoggerFactory.getLogger(ProductionConfig.class);

    @Override
    public DatabaseInformation getDatabaseInformation() {
        return new DatabaseInformation(env.getRequiredProperty(Constants.ENV_DB_CONNSTR),
                env.getRequiredProperty(Constants.ENV_DB_NAME));
    }

    /**
     * Gets the appInsights telemetry configuration information
     * @return telemetry key
     */
    @Bean
    public String telemetryConfig() {
        // note: this is optional, if it isn't set we won't use appInsights
        String telemetryKey = env.getProperty(Constants.ENV_APPINSIGHTS_KEY);
        if (telemetryKey != null) {
            TelemetryConfiguration.getActive().setInstrumentationKey(telemetryKey);
            logger.info(Constants.STATUS_APPINSIGHTS_SUCCESS);
        } else {
            logger.info(Constants.STATUS_APPINSIGHTS_FAILURE);
        }
        return telemetryKey;
    }


    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        // In production mode, we ignore webSecurity features for /health endpoint
        // effectively disabling oauth2 token requirements for health probe
        webSecurity.ignoring().antMatchers("/health");
    }

    /**
     * Creates bean of type WebRequestTrackingFilter for request tracking to appInsights
     * @return {@link Bean} of type {@link WebRequestTrackingFilter}
     */
    @Bean
    public WebRequestTrackingFilter appInsightFilter() {
        return new WebRequestTrackingFilter(Constants.APP_NAME);
    }
}
