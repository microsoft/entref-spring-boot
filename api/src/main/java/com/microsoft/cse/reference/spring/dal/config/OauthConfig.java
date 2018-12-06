package com.microsoft.cse.reference.spring.dal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

/**
 * Configure oauth2 resource server
 */
@Configuration
@ConditionalOnProperty(name = {Constants.ENV_OAUTH_KEYSET_URI, Constants.ENV_OAUTH_RES_ID})
public class OauthConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    Environment env;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // setup the resource id
        resources.resourceId(this.env.getProperty(Constants.ENV_OAUTH_RES_ID));

        // setup the token store
        resources.tokenStore(new JwkTokenStore(this.env.getProperty(Constants.ENV_OAUTH_KEYSET_URI)));
    }
}
