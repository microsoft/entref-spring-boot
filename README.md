# SpringDAL

> Looking to get running quickly? Jump ahead to our [quickstart](#quickstart).

A RESTful DAL (Database Abstraction Layer) reference implementation written using Spring.

# Introduction

This project provides a reference implementation for of Java-based microservices with REST APIs that read and write data stored in Azure Cosmos DB. The services are hosted in containers running in Azure App Service for Containers, (FUTURE: with Azure Redis providing caching). HA/DR is provided by hosting the microservices in multiple regions, as well as CosmosDB's native geo-redundancy. Traffic Manager is used to route traffic based on geo-proximity, and Application Gateway provides path-based routing, service authentication and DDoS protection.

Cosmos DB is configured to use the NoSQL MongoDB API.

In order to demonstrate Cosmos DB performance with large amounts of data, the project imports historical movie data from [IMDb](https://www.imdb.com/interfaces/). See (https://datasets.imdbws.com/). The datasets include 8.9 million people, 5.3 million movies and 30 million relationships between them.

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

We decided to host our application using Azure App Services instead of using Azure Kubernetes Cluster. We made this decision because Azure App Services gave us better control over scaling the app accross regions. It also required less configuration with our traffic manager and load balancer architecture. Furthermore, Azure App Services has an easy-to-use, built-in load testing service that we utilize to test the container scaling of our app. Out of the box, Azure App Services offers auto-scaling, authentication, and deployment slots. In the future, because Azure App Services is a PaaS provider, we can implement [Platform Chaos](https://github.com/Azure/platform-chaos) to initiate chaos testing services too. While this approach does not provide as much control of the server itself, the deployed docker container will keep the JVM consistent across deployments.

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
| Enhanced productivity via Docker| Microservices implemented in Docker containers, which are hosted by the Azure App Service for Containers PaaS service. Developer productivity enhanced due to service isolation and easy service updates
| Example of well-designed CI/CD pipeline | Full continuous integration/continuous delivery (CI/CD) is implemented using Azure DevOps with a pipeline of environments that support dev, testing and production
| Automated infrastructure deployment | <li>Azure ARM templates<li>App Service for Containers<li>Azure container registry
| High Availability/Disaster Recovery (HA/DR) | Full geo-replication of microservices and data, with automatic failover in the event of an issue in any region:<br><br><li>Cosmos DB deployed to multiple regions with active-active read/write<li>Session consistency to assure that user experience is consistent across failover<li>Stateless microservices deployed to multiple regions<li>Health monitoring to detect errors that require failover<li>Azure Traffic Manager redirects traffic to healthy region
| Demonstrates insfrastructure best practices | <li>Application auto-scaling<li>Minimize network latency through geo-based DNS routing<li>API authentication<li>Distributed denial of service (DDoS) protection & mitigation
| Load and performance testing | The solution includes an integrated traffic simulator to demonstrate that the solution auto-scales properly, maintaining application performance as scale increases
| Proves application resiliency through chaos testing | A Chaos Monkey-style solution to shut down different portions of the architecture in order to validate that resilience measures keep everything running in the event of any single failure

## Quickstart

This project is composed of many different pieces - This section is designed to get you up and running as quickly as possible.

* The largest component of this service is the Java Backend - see [the Backend Readme](./api/README.md)
* Our UI component is a separate service that's built using React and Webpack - see [the UI Readme](./ui/README.md)
* To scale our service on Azure we leverage ARM templates - see [the Infrastructure Readme](./infrastructure/README.md)

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
