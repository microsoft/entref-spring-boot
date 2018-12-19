package com.microsoft.cse.reference.spring.dal.config;

public class Constants {
    /**
     * The constant that represents our application name
     */
    public static final String APP_NAME = "SpringDAL";

    /**
     * The constant environment variable that we look to for the oauth2 resource id
     * For more info, see: https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-oauth2-resource-server
     */
    public static final String ENV_OAUTH_RES_ID = "OAUTH_RES_ID";

    /**
     * The constant environment variable that we look to for the oauth2 keyset uri
     * For more info, see: https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-oauth2-resource-server
     */
    public static final String ENV_OAUTH_KEYSET_URI = "OAUTH_KEYSET_URI";

    /**
     * The constant environment variable that we look to for production db connection string
     */
    public static final String ENV_DB_CONNSTR = "DB_CONNSTR";

    /**
     * The constant environment variable that we look to for production db name
     */
    public static final String ENV_DB_NAME = "DB_NAME";

    /**
     * The constant environment variable that we look to for exclusion filters
     * Note: this value may be a comma separated list (ie: ClassA,ClassB)
     * Note: regex is supported in each entry
     */
    public static final String ENV_EXCLUDE_FILTER = "EXCLUDE_FILTER";
    
    /**
     * The constant environment variable that we look to for app insights telemetry key
     * for more information, see the following:
     * https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started#1-get-an-application-insights-instrumentation-key
     */
    public static final String ENV_APPINSIGHTS_KEY = "APPLICATION_INSIGHTS_IKEY";

    /**
     * The constant environment variable that we look to for the allowed origins strings for CORS
     */
    public static final String ENV_ALLOWED_ORIGIN = "ALLOWED_ORIGIN";

    /**
     * Error message used to indicate we couldn't read database configuration
     */
    public static final String ERR_DB_CONF = "Failed to read database information from configuration";

    /**
     * Error message used to indicate we couldn't read test data
     */
    public static final String ERR_TEST_DATA_FAIL = "Failed to read test data";

    /**
     * Error message used to indicate we couldn't properly parse test data
     */
    public static final String ERR_TEST_DATA_FORMAT = "Json structure must be a top-level array";

    /**
     * Status message that is used to display our database connection information
     * Note: should String.format({0}=database info)
     */
    public static final String STATUS_DB_CONN_INFO = "Successfully read database configuration %s";

    /**
     * Status message that is used to indicate we've loaded test data
     */
    public static final String STATUS_TEST_DATA_USED = "Successfully loaded test data";

    /**
     * Status message that is used to indicate we've configured appInsights
     */
    public static final String STATUS_APPINSIGHTS_SUCCESS = "Successfully configured appInsights telemetry key";

    /**
     * Status message that is used to indicate we've failed to configure appInsights:
     * Note: this isn't an ERR, as appInsights is optional in all cases today
     */
    public static final String STATUS_APPINSIGHTS_FAILURE = "Unable to configure appInsights telemetry key";

    /**
     * The collection from which we pull Person objects
     */
    public static final String DB_PERSON_COLLECTION = "names";

    /**
     * The collection from which we pull Product objects
     */
    public static final String DB_PRODUCT_COLLECTION = "products";

    /**
     * The collection from which we pull Cart objects
     */
    public static final String DB_CART_COLLECTION = "carts";
}
