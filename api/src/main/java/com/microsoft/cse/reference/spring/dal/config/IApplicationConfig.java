package com.microsoft.cse.reference.spring.dal.config;

public interface IApplicationConfig {
    /**
     * Get the database information object that describes the database we'll connect to
     * @return Database information object
     * @throws Exception Thrown when we cannot get this information (usually due to misconfiguration)
     */
    DatabaseInformation getDatabaseInformation() throws Exception;
}
