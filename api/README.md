# Project Jackson API

This contains the Backend API, written in [Java](https://www.java.com) using the [Spring Framework](https://spring.io).

# Quickstart

> Are [IDEs](https://en.wikipedia.org/wiki/Integrated_development_environment) more your style? Skip ahead to our [IDE Quickstart](#quickstart-with-ide).

* 🔄 [Clone](https://www.git-scm.com/docs/git-clone) the repository using [Git](https://git-scm.com/downloads)
```
git clone https://github.com/Microsoft/containers-rest-cosmos-appservice-java.git
```
* 🏗 Install [Oracle JDK8u191](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to help us build Java code
* 📦 Install [Maven 3.6.0](https://maven.apache.org/install.html) to help manage our dependencies
* 📝 (Optionally) Configure necessary [Application Configuration](#application-configuration) values as [Environment variables](https://en.wikipedia.org/wiki/Environment_variable)
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
* 📝 (Optionally) Configure necessary [Application Configuration](#application-configuration) values as [Environment variables](https://en.wikipedia.org/wiki/Environment_variable)
* ▶️ [Run the application](https://www.jetbrains.com/help/idea/running-applications.html)

Note: Running the application will use [Maven](https://maven.apache.org/) to install all dependencies, and then use [Java](https://www.java.com/) to run the application.

# Quickstart with our mock data

- [Create a resource group](https://docs.microsoft.com/en-us/azure/azure-resource-manager/resource-group-portal#manage-resource-groups)
- [Add a Cosmos DB instance](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java#create-a-database-account) to the resource group
  - When selecting an API, choose `Core (SQL)` if you need a NoSQL solution with the ability to query using SQL
- Set up environment variables
  - From Bash command line, run `load_env.sh`. It will will write/load any needed variables to the `vars.env` file. 
    - `RESOURCE_GROUP` - the Azure resource group name
    - `COSMOSDB_NAME` - the CosmosDB collection name (which is case sensitive)
    - `COSMOSDB_PASSWORD` - the CosmosDB's password (needed for when you load the data into Cosmos)
  - Load `vars.env` into your environment or VM where the app is being built locally.
    - `source vars.env`
    - or in your chosen IDE, set your environment variables within your project.
  

# Deploying to Production

In the Quickstart sections above, we covered how to run this reference solution locally. In this next section, we'll discuss how to deploy the solution to Azure to simulate a scalable production environment.

When running in a production environment, there are a number of required [Application Configuration](#application-configuration) values that must be defined. Please see the [Application Configuration](#application-configuration) section for more information.

To deploy a production instance of this service to Azure, we recommend reading the [infrastructure README.md](../infrastructure/README.md) file, which features an easy deploy to Azure button. Please note that we also recommend the use of a Continuous Delivery pipeline to manage scalable deployments to Azure. A walk-through is [in the works](https://github.com/Microsoft/containers-rest-cosmos-appservice-java/issues/24).

## Application Configuration

We use [Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) for configuration.
Currently `development` and `production` are the two possible values for the `spring.profiles.active` property.
By default, `development` is assumed. Note: `default` is technically it's own profile, which is the same as `development`. The following sections document the possible configuration values as logically grouped sets.

### Authentication

> Note: If you're running with the `development` profile, this is __optional__.

To configure authentication, you'll need to specify your authentication provider's `jwt` or `jwk` key uri. For more information see [the spring docs](https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-oauth2-resource-server).

The `OAUTH_KEYSET_URI` environment variable must be set to that uri. For Microsoft Azure, that value can always be `https://login.microsoftonline.com/common/discovery/keys` - this is because a common key set is used for all Azure Active Directory applications.

The `OAUTH_RES_ID` environment variable should (but optionally may not be) set to the application id from the oauth2 provider. If this is omitted the authentication layer will validate whether the token is created by the given provider, but not that it is issued for your specific application.

Learn more about how to configure an Azure Active Directory application [here](../docs/azureActiveDirectory.md).

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

Before any downloading make sure that you have you have the environment variables loaded (preferably in a shell); to so run `load_env.sh`. 

1. Run `data/getdata.sh` in Bash or your prefered shell. This downloads a dataset from IMDb for the people in our scenario, and also downloads a product dataset from the USDA.
2. Use the Azure Portal to create the database `jacksonDatabase` with three collections: `products`, `names`, and `carts`.
3. During upload you may want to increase the throughput value - maybe as high as 100,000 - but remember to lower it back down once you're done to avoid over-charging! 
    - Read more [here.](https://docs.microsoft.com/en-us/azure/cosmos-db/set-throughput)

Follow the steps on Azure Cosmos DB's [import documentation for CSVs](https://docs.microsoft.com/en-us/azure/cosmos-db/import-data#CSV):
1. Download the DocumentDB Data Migration tool (see the link above for download).
2. For the `names.basic.tsv` file, it is recommended to edit the file from TSV into a CSV, using something like Excel to make certain columns into text fields.
    - Feel free to make this change in a Command Line editor like emacs or vim. Some fields have commas, so you want to make sure they're encapsulated as a string.
3. Upload your files using the DocumentDB Data Migration tool
    - The link provided at the beginning of this list also provides steps for the CLI, which we will not cover here.
4. In the Source Information section, select 'CSV file(s)' from the 'Import From' dropdown; enter a ',' (comma) for the 'Nesting Separator.'
5. In the Target Information section, enter your database connection string, appending `;Database=jacksonDatabase` to the end; verify by clicking the button.
6. Associate with a collection ([made separately](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java)) that corresponds to the data being imported.
7. The partition key maybe the unique identifier for the dataset; for the `products` collection, it's the 'NDBNumber' and for `names` it's the 'nconst' fields.

### Logging

Spring uses [Commons Logging](https://commons.apache.org/logging) under the hood, more details can be found
[here](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html).
To configure logging, the following environment variables can be used:

> Note: These values should be a [valid log level](a valid [log level](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html#boot-features-custom-log-levels))

+ `logging.level.root` - Configures the logging level for the whole application, including frameworks
+ `logging.level.com.microsoft.cse.*` - Configures the logging level for our application, excluding frameworks
+ `logging.level.org.zalando.logbook` - Configures the logging of HTTP requests/responses to the console. *Set to TRACE*

To configure [application insights](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-overview) logging, the following environment variable must be set:

+ `APPLICATION_INSIGHTS_IKEY` - an [Application Insights telemetry key](https://docs.microsoft.com/en-us/azure/application-insights/app-insights-java-get-started#1-get-an-application-insights-instrumentation-key)

## Testing

To run the tests, use `mvn test`. This project strives to unit test each behavior, and integration test end to end scenarios.