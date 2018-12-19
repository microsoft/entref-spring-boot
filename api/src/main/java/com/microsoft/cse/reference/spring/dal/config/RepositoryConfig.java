package com.microsoft.cse.reference.spring.dal.config;

import com.microsoft.cse.reference.spring.dal.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import java.util.regex.Pattern;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
    @Autowired
    Environment env;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // expose the ids for the given model types
        config.exposeIdsFor(Person.class);

        // configure how we find and load repositories to let us disable them at runtime with an environment variable
        config.setRepositoryDetectionStrategy(metadata -> {
            // if it's not exported, exclude it
            if (!metadata.getRepositoryInterface().getAnnotation(RepositoryRestResource.class).exported()) {
                return false;
            }

            String className = metadata.getRepositoryInterface().getName();
            String exclusionList = env.getProperty(Constants.ENV_EXCLUDE_FILTER);

            if (exclusionList != null && !exclusionList.isEmpty()) {
                for (String exclude : exclusionList.split(",")) {
                    // see if we get any hits, treating the exclusion list entry as a regex pattern
                    // note: this allows us to hit 'ClassA' even if it's really 'com.package.ClassA'
                    if (Pattern.compile(exclude).matcher(className).find()) {
                        // exclude if we match
                        return false;
                    }
                }
            }

            // default to allowing the repository
            return true;
        });
    }
}