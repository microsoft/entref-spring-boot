# SpringDAL

> Looking to get running quickly? Jump ahead to our [quickstart](#quickstart).

A RESTful DAL (Database Abstraction Layer) reference implementation written using Spring.

# Introduction

This project provides a reference implementation for of Java-based microservices with REST APIs that read and write data stored in Azure Cosmos DB. The services are hosted in containers running in Azure App Service for Containers, (FUTURE: with Azure Redis providing caching). HA/DR is provided by hosting the microservices in multiple regions, as well as CosmosDB's native geo-redundancy. Traffic Manager is used to route traffic based on geo-proximity, and Application Gateway provides path-based routing, service authentication and DDoS protection.

Cosmos DB is configured to use the NoSQL MongoDB API.

In order to demonstrate Cosmos DB performance with large amounts of data, the project imports historial movie data from [IMDb](https://www.imdb.com/interfaces/). See (https://datasets.imdbws.com/.) The datasets include 8.9 million peple, 5.3 million movies and 30 million relationships between them.

## Architecture

This solution provides a robust foundation on which enterprise engineering (EE) teams may build and deploy production-ready microservices solutions.

We built the solution to provide a common enterprise-ready foundation for Azure-based applications with the following architecture:

- Java-based microservices
- Data stored in Cosmos DB
- Redis-based caching
- High Availability & Disaster Recovery (HA/DR)
- A full CI/CD pipeline
- Robust but simple codebase that follows common enterprise-engineering best practices
- Load and failure simulators to validate scale, resiliency and failover

### API Routes

We're using three kinds of models: `Person`, `Title`, and `Principal`. The `Person` model represents a person who participates in media, either in front of the camera or behind the scenes. The `Title` represents what it sounds like - the title of the piece of media, be it a movie, a TV series, or some other kind of similar media. Finally, the `Principal` model and its derivative child class `PrincipalWithName` represent the intersection of Person and Title, ie. what a particular person does or plays in a specific title.

To meaningfully access this IMDb dataset and these models, there are a few routes one can access on the API.

+ `/people`
  + `POST` - Creates a person, and returns information and ID of new person
  + `GET` - Returns a small number of people entries
+ `/people/{nconst}` > nconst is the unique identifier
  + `GET` - Gets the person associated with ID, and returns information about the person
  + `PUT` - Updates a person for a given ID, and returns information about updated person
  + `DELETE` - Deletes a person with a given ID, and returns the success/failure code
+ `/people/{nconst}/titles` > nconst is the unique identifier
  + `GET` - Gets the titles in the dataset associated with the person with specified ID and returns them in an array
+ `/titles`
  + `POST` - Creates a title, and returns the information and ID of the new titles
  + `GET` - Returns a small number of title entries
+ `/titles/{tconst}` > tconst is the unique identifier
  + `GET` - Gets the title of piece given the ID, and returns information about that title
  + `PUT` - Updates the title of a piece given the ID, and returns that updated information based on ID
  + `DELETE` - Deletes the piece of media given the ID, and returns the success/failure code
+ `/titles/{tconst}/people` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title, and returns that list
+ `/titles/{tconst}/cast` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title who act, and returns that list
+ `/titles/{tconst}/crew` > tconst is the unique identifier
  + `GET` - Gets the people in the dataset associated with the given title who participate behind the scenes, and returns that list

For more details, check out the [Swagger documentation](./api/swagger.yml).

### Why We Chose App Services

We decided to host our application using Azure App Services instead of using Azure Kuberenetes Cluster. We made this decision because Azure App Services gave us better control over scaling the app accross regions. It also required less configuration with our traffic manager and load balancer architecture. Furthermore, Azure App Services has an easy-to-use, built-in load testing service that we utilize to test the container scaling of our app. Out of the box, Azure App Services offers auto-scaling, authentication, and deployment slots. In the future, because Azure App Services is a PaaS provider, we can implement Platform Chaos to initiate chaos testing services too. While this approach does not provide as much control of the server itself, the deployed docker container will keep the JVM consistent across deployments.

If you'd like to learn more you can read these articles:
 - [Container? Why not App Services?](https://blogs.msdn.microsoft.com/premier_developer/2018/06/15/container-why-not-app-services/)
 - [Azure Deployment Models](https://stackify.com/azure-deployment-models/)

## Key Benefits

Key technologies and concepts demonstrated:

| Benefit | Supporting Solution
|---|---
| Common, standard technologies | <li>Java programming language<li>Spring Boot Framework, one of the most widely used EE frameworks for Java<li>MongoDB NoSQL API (via Azure Cosmos DB)<li>Redis Cache
| Production-ready codebase | High quality codebase that is easily enhanced, well-documented and meets typical enterprise code quality standards
| Well-designed RESTful API | Solution follows RESTful design best-practices
| Enhanced productivity via Docker| Micros-services implemented in Docker containers, which are hosted by the Azure App Service for Containers PaaS service. Developer productivity enhance due to service isolation and easy service updates
| Example of well-designed CI/CD pipeline | Full continuous integration/continuous delivery (CI/CD) is implemented using Azure DevOps with a pipeline of environments that support dev, testing and production
| Automated infrastructure deployment | <li>Azure ARM templates<li>App Service for Containers<li>Azure container registry
| High Availability/Disaster Recovery (HA/DR) | Full geo-replication of microservices and data, with automatic failover in the event of an issue in any region:<br><br><li>Cosmos DB deployed to multiple regions with active-active read/write<li>Session consistency to assure that user experience is consistent across failover<li>Stateless microservices deployed to multiple regions<li>Health monitoring to detect errors that require failover<li>Azure Traffic Manager redirects traffic to healthy region
| Demonstrates insfrastructure best practices | <li>Application auto-scaling<li>Minimize network latency through geo-based DNS routing<li>API authentication<li>Distributed denial of service (DDoS) protection & mitigation
| Load and performance testing | The solution includes an integrated traffic simulator to demonstrate that the solution auto-scales properly, maintaining application performance as scale increases
| Proves application resiliency through chaos testing | A Chaos Monkey-style solution to shut down different portions of the architecture in order to validate that resilience measures keep everything runing in the event of any single failure

# Quickstart

NIT: Move to api subfolder, reference here
Same for ui :smile:

> Are [IDEs](https://en.wikipedia.org/wiki/Integrated_development_environment) more your style? Skip ahead to our [IDE Quickstart](#quickstart-with-ide).

* 🔄 [Clone](https://www.git-scm.com/docs/git-clone) the repository using [Git](https://git-scm.com/downloads)
```
git clone https://github.com/Microsoft/containers-rest-cosmos-appservice-java.git
```
* 🏗 Install [JDK8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to help us build Java code
* 📦 Install [Maven](https://maven.apache.org/install.html) to help manage our dependencies
* ⚙️ Ensure JDK8 tools, and Maven are [in your path](https://java.com/en/download/help/path.xml) (typically done for you on Windows and Mac OS X)
```
> # Validate Maven is installed by attempting to query its version
> mvn --version
Apache Maven 3.5.4 (1edded0938998edf8bf061f1ceb3cfdeccf443fe; 2018-06-17T14:33:14-04:00)
Maven home: c:\bin\maven\bin\..
Java version: 1.8.0_181, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jre1.8.0_181
Default locale: en_US, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

> # Validate Javac (The Java Compiler) is installed by attempting to query it's version
> javac --version
javac 1.8.0_181
```
* 📝 Configure necessary [Application Configuration](#application-configuration) values as [Environment variables](https://en.wikipedia.org/wiki/Environment_variable)
```
> # On Windows we can use:
> set spring.profiles.active=development

> # On Linux we can use:
> export spring.profiles.active=development
```
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
* 🏗 Install [JDK8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to help us build Java code
* 📦 Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download)
* 💡 Open IDEA and select "Open Project", choosing `api/` from the project directory as the IDEA project location
* ⚙️ In the bottom right, IDEA will tell you "Maven projects need to be imported" - select "Import Changes"
* 📓 Create a [Run Configuration](https://www.jetbrains.com/help/idea/run-debug-configurations-dialog.html) for Maven, specifying `spring-boot:run` as the "Command Line" value in the "Parameters" pane
```
Command Line: spring-boot:run
```
* 🔠 Add the necessary [Application Configuration](#application-configuration) values as [Environment Variables](https://en.wikipedia.org/wiki/Environment_variable) in the "Runner" pane under "Environment variables"
```
Key: spring.profiles.active, Value: development
```
* ▶️ [Run the application](https://www.jetbrains.com/help/idea/running-applications.html), by selecting the ▶️ button in the top right (hint: It's a green play button) 

Note: Running the application will use [Maven](https://maven.apache.org/) to install all dependencies, and then use [Java](https://www.java.com/) to run the application.

## Deploying to Production

In the Quickstart sections above, we covered how to run this reference solution locally. In this next section, we'll discuss how to deploy the solution to Azure to simulate a scalable production environment.

When running in a production environment, there are a number of required [Application Configuration](#application-configuration) values that must be defined. Please see the [Application Configuration](#application-configuration) section for more information.

To deploy a production instance of this service to Azure, we recommend reading the [infrastructure README.md](./infrastructure/README.md) file, which features an easy deploy to Azure button. Please note that we also recommend the use of a Continuous Delivery pipeline to manage scalable deployments to Azure. A walk-through is [in the works](https://github.com/Microsoft/containers-rest-cosmos-appservice-java/issues/24).

## Application Configuration

We use [Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) for configuration.
Currently `development` and `production` are the two possible values for the `spring.profiles.active` property.
By default, `development` is assumed. Note: `default` is technically it's own profile, which is the same as `development`. The following sections document the possible configuration values as logically grouped sets.

### Authentication

> Note: If you're running with the `development` profile, this is __optional__.

To configure authentication, you'll need to specify your authentication provider's `jwt` or `jwk` key uri. For more information see [the spring docs](https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-oauth2-resource-server).

The `OAUTH_KEYSET_URI` environment variable must be set to that uri. For Microsoft Azure, that value can always be `https://login.microsoftonline.com/common/discovery/keys` - this is because a common key set is used for all Azure Active Directory applications.

The `OAUTH_RES_ID` environment variable should (but optionally may not be) set to the application id from the oauth2 provider. If this is omitted the authentication layer will validate whether the token is created by the given provider, but not that it is issued for your specific application.

Learn more about how to configure an Azure Active Directory application [here](./docs/azureActiveDirectory.md).

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

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
