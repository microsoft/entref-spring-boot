# Containerized Java REST Services on Azure App Service with a CosmosDB backend

## Project Health

API Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-API-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=22?branchName=master)

UI Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-UI-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=25?branchName=master)

Infrastructure Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-Infrastructure-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=23?branchName=master)

## Contents:

* [Introduction & Overiew (this document)](Introduction)
* [Quick Start for Developers](./GettingStarted.md)
* [Sample Application and REST APIs](./RESTAPI.md)

## Introduction

This project was created to demonstrate end-to-end best practices building and running "enterprise-class"
applications on Azure. This document explains what the project provides and why, and it provides instructions for getting started.

## Enterprise-Class Applications Defined

We are using the term "enterprise-class app" to refer to an end-to-end solution that delivers the following 
capabilities:

* **Horizontal scalability:** Add capacity by adding additional containers and/or VMs
* **Infrastructure as code:** Create and manage Azure environments using template code that is under source control
* **Agile engineering and rapid updates:** CI/CD is used to automated builds, tests and deployments, ensuring that developers cansafely check-in updates continuously and allow frequent production updates to the production environment and application.
* **High Availability:** The application, as well as all infrastructure, is some combination of stateless and/or redundant so that everything continues running normally when any single component fails or otherwise goes offline
* **Blue/Green (aka Canary Deployments:** Updates are initally rolled out to a "green" application instance, while the existing deployment continues to run on the "blue" instance. The green instance is intially exposed to only a small number of users. Monitoring is performed to look for any degradations in service related to the green instance. If everything looks good, traffic is gradually diverted to the green instance. Should the service quality degrade,the deployment is rolled back by returning all traffic to the blue instance.
* **Hardened:** Enterprise applications must be instrinsically resistant to attacks from bad actors, such as Distributed Denial of Service (DDoS) attacks.
* **Networking compliance:** All enterpises takes steps to ensure the privacy and security of their networks. Enterprise solutions on Azure must support common networking requirements, such as the use of ExpressRoute to communicate with the enterprises's data-centers and/or on-premises networks, and private IPs for all but public end-points.
* **Testable:** No production solution can truly be trusted to be reliable unless it is continously tested to validate scalability, resilence and security.  

## OSS Technology Choices

Our team, Commercial Software Engineering (CSE), collaboratively codes with Microsoft's biggest and most important customers.
We see a huge spectrum of technology choices at different customers, ranging from all-Microsoft to all-OSS. Most comonly, we see a mix.

Given the wide range of technology choices out there, it's difficult to create a one-size-fits-all reference solution. For this particular project, we selected a set of technologies that are of interest to many of our customers.

This OSS solution uses the following OSS technologies:

* **GitHub:** This project is self-evidently published to GitHub, but it's also a deliberate choise we made. GitHub is both the most recognized place to publish OSS projects, but it also has phenomenal tools to enable community contributions.
* **Docker:** Though there are other container technologies out there, Docker/Moby is pretty much synonymous with the idea.
* **Java Version 8 (1.8.x):** A very common choice of programming langauages my many enterpises
* **Spring Boot:** One of the most widely used and capable Java frameworks
* **Spring Data REST:** A simple way to build REST APIs in a Spring Boot application that are backed by a persistent data repository.
* **Maven:** A commonly used tool for building and managing Java projects

## Azure Technologies & Services

As with our OSS technology choices, we intentionally selected a set of Azure technologies and services that support common enterprise requirements, including:

* **Azure DevOps:** Microsoft's CI/CD solution, which is the Azure-branded version of Microsoft's mature and widely used VSTS solution.
* **Azure Resource Manager (ARM):** Azure's solution for deploying and managing Azure resources via JSON-based templates
* **App Services:** A robust platform-as-a-service (PaaS) solution for application hosting. App Services hides the complexity of provisioning and managing VMs, auto-scaling, creating public IPs, etc.

>**Note:** App Services is appropriate for a wide range of enterprise apps, including certain highly scaled apps, though we often recommend 
>Azure Kubernetes Service (AKS) for apps that require certain advanced capabilities.

* **Cosmos DB:** Cosmos DB is perhaps the fastest and most reliable NoSQL data storage service in the world. It is an excellent choice when performance and reliability are a must, and when enterprises require multi-region write capabilities, which are essential for both application/service performance and for HA/DR scenarios.
* **Azure Traffic Manager:**
* **Application Gateway:**
* **App Insights:** Enterprise developers use App Insights to monitor and detect performance anomolies in production applications.

The solution leverages Azure Dev Ops for Continuous Integration 
and Delivery (CI/CD), and it deploys complete Azure environments via Azure Resource Manager (ARM) templates.

## Architecture

This solution provides a robust foundation on which enterprise engineering (EE) teams may build and deploy production-ready microservices solutions.

We built the solution to provide a common enterprise-ready foundation for Azure-based applications with the following architecture:

* Java-based microservices
* Data stored in Cosmos DB
* High Availability & Disaster Recovery (HA/DR)
* A full CI/CD pipeline
* Robust but simple codebase that follows common enterprise-engineering best practices
* Load and failure simulators to validate scale, resiliency and failover

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

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
