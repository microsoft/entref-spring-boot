# Project Jackson API

This readme contains the Backend API, written in [Java](https://www.java.com) using the [Spring Framework](https://spring.io).

# Quickstart

> Are [IDEs](https://en.wikipedia.org/wiki/Integrated_development_environment) more your style? Skip ahead to our [IDE Quickstart](#quickstart-with-ide).

* 🔄 [Clone](https://www.git-scm.com/docs/git-clone) the repository using [Git](https://git-scm.com/downloads)
```
git clone https://github.com/Microsoft/containers-rest-cosmos-appservice-java.git
```
* 🏗 Install [Oracle JDK8u191](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to help us build Java code
* 📦 Install [Maven 3.6.0](https://maven.apache.org/install.html) to help manage our dependencies
* 📝 (Optional) Configure necessary [Application Configuration](#application-configuration) values as [Environment variables](https://en.wikipedia.org/wiki/Environment_variable)
* 🏃‍♀️ Build and Run from your project directory (created when you cloned, typically `containers-rest-cosmos-appservice-java`)
```
mvn spring-boot:run
```

Note: Running will use [Maven](https://maven.apache.org/) to install all dependencies, and then use [Java](https://www.java.com/) to run the application.

# Quickstart with IDE

> We recommend using [IDEA](https://www.jetbrains.com/idea/) so these steps assume the use of that IDE.

* 🔄 [Clone](https://www.git-scm.com/docs/git-clone) the Repository using [Git](https://git-scm.com/downloads)
```
git clone https://github.com/Microsoft/containers-rest-cosmos-appservice-java.git
```
* 🏗 Install [Oracle JDK8u191](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to help us build Java code
* 📦 Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
* 💡 Open IDEA and select "Open Project", choosing `api/` from the project directory as the IDEA project location
* ⚙️ In the bottom right, IDEA will tell you "Maven projects need to be imported" - select "Import Changes"
* 📓 Create a [Run Configuration](https://www.jetbrains.com/help/idea/run-debug-configurations-dialog.html) for Maven, specifying `spring-boot:run` as the "Command Line" value in the "Parameters" pane
```
Command Line: spring-boot:run
```
* 📝 (Optional) Configure necessary [Application Configuration](#application-configuration) values as [Environment variables](https://en.wikipedia.org/wiki/Environment_variable)
* ▶️ [Run the application](https://www.jetbrains.com/help/idea/running-applications.html)

Note: Running the application will use [Maven](https://maven.apache.org/) to install all dependencies, and then use [Java](https://www.java.com/) to run the application.

## Deploying to Production

In the Quickstart section above, we covered how to run this reference solution locally. In this  section, we'll discuss how to deploy the solution to Azure to simulate a scalable production environment.

When running in a production environment, there are a number of required [Application Configuration](#application-configuration) values that must be defined. Please see the [Application Configuration](#application-configuration) section for more information.

To deploy a production instance of this service to Azure, we recommend reading the [infrastructure README.md](../infrastructure/README.md) file, which features an easy "deploy to Azure" button. We also recommend the use of a Continuous Delivery pipeline to manage scalable deployments to Azure. A walk-through is [in the works](https://github.com/Microsoft/containers-rest-cosmos-appservice-java/issues/24).

## Application Configuration

We use [Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) for configuration.
Currently `development` and `production` are the two possible values for the `spring.profiles.active` property.
By default, `development` is assumed. Note: `default` is technically it's own profile, which is the same as `development`. The following sections document the possible configuration values as logically grouped sets.

### Authentication

> Note: If you're running with the `development` profile, this is __optional__.

To configure authentication, you'll need to specify your authentication provider's `jwt` or `jwk` key uri. For more information see [the spring docs](https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-oauth2-resource-server).

The `OAUTH_KEYSET_URI` environment variable must be set to that uri. For Microsoft Azure, that value can always be `https://login.microsoftonline.com/common/discovery/keys` - this is because a common key set is used for all Azure Active Directory applications.

The `OAUTH_RES_ID` environment variable should (but optionally may not be) set to the application id from the oauth2 provider. If this is omitted, the authentication layer will validate whether the token is created by the given provider, but not that it is issued for your specific application.

Learn more about how to configure an Azure Active Directory application [here](../docs/azureActiveDirectory.md).

 ### Azure Resources
 To configure environmental variables in Azure Resources:
 
 + Open the blade for the application in the [Azure Portal](https://portal.azure.com/)
 + Click on **Application Settings**
 + Under the *Application Settings* category, there are key/value pairs that your app will load on start up. Additional information provided [here](https://docs.microsoft.com/en-us/azure/app-service/web-sites-configure#app-settings).
 

### Database

> Note: If you're running with the `development` profile, this is __optional__.

To configure communications with a database, the following environment variables are used:

+ `DB_CONNSTR` - a mongo [database connection string](https://docs.mongodb.com/manual/reference/connection-string/) (ex: `mongodb://db.com/myDb`)
+ `DB_NAME` - a mongo database name (ex: `myDb`)
+ `EXCLUDE_FILTER` - [optional] a (regex capable) list of classes to exclude from loading (ex: `TitleRepository,PersonRepository`)

### Mock Data

By default, when running with the `development` profile, test data is auto-loaded into the embedded mongo instance.
However, __if you set the above environment variables, that configuration will take precedence__.

This mock data contains about 8 entries from each collection, and can be found in the `src/main/resources/testdata` folder. There are related entries across each collection to prove out the custom API routes.

### Logging

Spring uses [Commons Logging](https://commons.apache.org/logging) under the hood, more details can be found
[here](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html).
To configure logging, the following environment variables can be used:

> Note: These values should be a [valid log level](a valid [log level](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html#boot-features-custom-log-levels))

+ `logging.level.root` - Configures the logging level for the whole application, including frameworks
+ `logging.level.com.microsoft.cse.*` - Configures the logging level for our application, excluding frameworks
+ `logging.level.org.zalando.logbook` - Configures the logging of HTTP requests/responses to the console. *Set to TRACE*
+ `logging.level.org.springframework.data.mongodb.core.MongoTemplate` - Configures the logging of a MongoDB query. *Set to DEBUG* to see how any constructed query gets data from MongoDB

To configure [application insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-overview) logging, the following environment variable must be set:

+ `APPLICATION_INSIGHTS_IKEY` - an [Application Insights telemetry key](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started#1-get-an-application-insights-instrumentation-key)

## Testing

To run the tests, use `mvn test`. This project strives to unit test each behavior, and integration test end to end scenarios.
