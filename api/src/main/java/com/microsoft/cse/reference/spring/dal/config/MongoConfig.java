package com.microsoft.cse.reference.spring.dal.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Configures the mongo driver based on our application configuration
 */
@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    IApplicationConfig appConfig;

    @Autowired
    List<Converter> converters;

    Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Override
    public MongoClient mongoClient() {
        DatabaseInformation info = null;
        try {
            info = this.appConfig.getDatabaseInformation();

            logger.info(String.format(Constants.STATUS_DB_CONN_INFO, info));
        } catch (Exception e) {
            logger.error(Constants.ERR_DB_CONF, e);
        }

        return new MongoClient(new MongoClientURI(info.getConnectionString()));
    }

    @Override
    protected String getDatabaseName() {
        DatabaseInformation info = null;
        try {
            info = this.appConfig.getDatabaseInformation();

            logger.info(String.format(Constants.STATUS_DB_CONN_INFO, info));
        } catch (Exception e) {
            logger.error(Constants.ERR_DB_CONF, e);
        }

        return info.getName();
    }

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(converters);
    }
}
