package com.microsoft.cse.reference.spring.dal.config;

/**
 * Represents a read-only database information object used for connection
 */
public class DatabaseInformation {
    private String connStr;
    private String name;

    /**
     * Create an instance of database information with the given connStr and name
     * @param connStr the connection string
     * @param name the name
     */
    public DatabaseInformation(String connStr, String name) {
        this.connStr = connStr;
        this.name = name;
    }

    /**
     * The connection string with which to connect to the database
     * @return connection string
     */
    public String getConnectionString() {
        return this.connStr;
    }

    /**
     * The name with which to connect to the database
     * @return name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return '[' + this.getConnectionString() + "]/" + this.getName();
    }
}
