# SpringDAL

> Looking to get running quickly? Jump ahead to our [quickstart](#quickstart).

A RESTful DAL (Database Abstraction Layer) reference implementation written using Spring.

# Introduction

This project provides a reference implementation for Java-based microservices with REST APIs that read and write data stored in Azure Cosmos DB. The services are hosted in containers running in Azure App Service for Containers, (FUTURE: with Azure Redis providing caching). HA/DR is provided by hosting the microservices in multiple regions, as well as CosmosDB's native geo-redundancy. Traffic Manager is used to route traffic based on geo-proximity, and Application Gateway provides path-based routing, service authentication and DDoS protection.

Cosmos DB is configured to use the NoSQL MongoDB API.

In order to demonstrate Cosmos DB performance with large amounts of data, the project imports historical movie data from [IMDb](https://www.imdb.com/interfaces/). See (https://datasets.imdbws.com/). The datasets include 8.9 million people, 5.3 million movies and 30 million relationships between them.

API Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-API-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=22?branchName=master)

UI Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-UI-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=25?branchName=master)

Infrastructure Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-Infrastructure-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=23?branchName=master)

## Architecture

This solution provides a foundation to build and deploy microservices solutions, using the following technologies: 

- Java-based microservices
- Data stored in Cosmos DB
- Redis-based caching
- High Availability & Disaster Recovery (HA/DR)
- CI/CD pipeline
- Load and failure simulators to validate scale, resiliency and failover

### API Routes

This solution uses three kinds of models: `Person`, `Title`, and `Principal`. The `Person` model represents a person who participates in media, either in front of the camera or behind the scenes. The `Title` represents the title of the piece of media, be it a movie, a TV series, or some other kind of media. Finally, the `Principal` model and its derivative child class `PrincipalWithName` represent the intersection of Person and Title, ie. what a particular person does or plays in a specific title.

To meaningfully access this IMDb dataset and these models, this solution provides the following API:

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

This solution uses Azure App Services instead of Azure Kubernetes Cluster because Azure App Services provides better control over scaling the app accross regions with less configuration in this scenario.   In addition, Azure App Services has an easy-to-use, built-in load testing service that we utilize to test the container scaling of our app. Out of the box, Azure App Services offers auto-scaling, authentication, and deployment slots. In the future, because Azure App Services is a PaaS provider, we can implement [Platform Chaos](https://github.com/Azure/platform-chaos) to do chaos testing. While this approach does not provide as much control of the server itself, the deployed docker container will keep the JVM consistent across deployments.

The following articles provide further details: 
 - [Container? Why not App Services?](https://blogs.msdn.microsoft.com/premier_developer/2018/06/15/container-why-not-app-services/)
 - [Azure Deployment Models](https://stackify.com/azure-deployment-models/)

## Key Benefits

Key technologies and concepts demonstrated:

| Benefit | Supporting Solution
|---|---
| Common, standard technologies | <li>Java programming language<li>Spring Boot Framework, one of the most widely used frameworks for Java<li>MongoDB NoSQL API (via Azure Cosmos DB)<li>Redis Cache
| Containerization | Microservices implemented in Docker containers, hosted by the Azure App Service for Containers PaaS service.
| CI/CD pipeline | Continuous integration/continuous delivery (CI/CD) is implemented using Azure DevOps with a pipeline of environments that support dev, testing and production
| Automated deployment | <li>Azure ARM templates<li>App Service for Containers<li>Azure container registry
| High Availability/Disaster Recovery (HA/DR) | Full geo-replication of microservices and data, with automatic failover in the event of an issue in any region:<br><br><li>Cosmos DB deployed to multiple regions with active-active read/write<li>Session consistency to assure that user experience is consistent across failover<li>Stateless microservices deployed to multiple regions<li>Health monitoring to detect errors that require failover<li>Azure Traffic Manager redirects traffic to healthy region
| Infrastructure best practices | <li>Application auto-scaling<li>Minimize network latency through geo-based DNS routing<li>API authentication<li>Distributed denial of service (DDoS) protection & mitigation
| Load and performance testing | The solution uses an integrated traffic simulator to demonstrate auto-scaling 
| Application resiliency | A Chaos Monkey-style solution that shuts down different portions of the architecture to validate that service-level resiliency
  
## Quickstart

This project is composed of three discrete pieces - This section is designed to get you up and running quickly.

* Java Backend - this is the largest component - see [Backend Readme](./api/README.md)
* UI component - built using React and Webpack - see [UI Readme](./ui/README.md)
* ARM template - to deploy and scale  - see [Infrastructure Readme](./infrastructure/README.md)

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
