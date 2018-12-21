# Containerized Java REST Services on Azure App Service with a CosmosDB backend

## Project Health

API Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-API-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=22?branchName=master)

UI Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-UI-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=25?branchName=master)

Infrastructure Build Status: [![Build Status](https://dev.azure.com/csebostoncrew/ProjectJackson/_apis/build/status/GitHub%20Builds/ProjectJackson-Infrastructure-GitHub?branchName=master)](https://dev.azure.com/csebostoncrew/ProjectJackson/_build/latest?definitionId=23?branchName=master)

## Contents:

* Introduction & Overiew (this document)
* [Quick Start for Developers](./GettingStarted.md)
* [Sample Application and REST APIs](./SampleApp.md)

## Introduction

This project was created to demonstrate end-to-end best practices building and running "enterprise-class"
applications on Azure. This document explains what the project provides and why, and it provides instructions for getting started.

## Enterprise-Class Applications Defined

We are using the term "enterprise-class app" to refer to an end-to-end solution that delivers the following 
capabilities:

* **Horizontal scalability:** Add capacity by adding additional containers and/or VMs
* **Infrastructure as code:** Create and manage Azure environments using template code that is under source control
* **Agile engineering and rapid updates:** Use CI/CD to automated builds, tests and deployments, ensuring that developers cansafely check-in updates continuously and allow frequent production updates to the production environment and application.
* **High Availability:** Design and deploy the application and infrastructure that everything continues running normally when any single component fails or otherwise goes offline.
* **Blue/Green (aka Canary Deployments):** Rollout updates to a "green" application instance, while the existing deployment continues to run on the "blue" instance. The green instance is intially exposed to only a small number of users. Monitoring is performed to look for any degradations in service related to the green instance. If everything looks good, traffic is gradually diverted to the green instance. Should the service quality degrade,the deployment is rolled back by returning all traffic to the blue instance.
* **Testable:** Continuously test the application in production to validate scalability, resilence and security.
* **Hardened:** Assure that the application and infrastructure is instrinsically resistant to attacks from bad actors, such as Distributed Denial of Service (DDoS) attacks.
* **Networking compliance:** Comply with enterprise network security requirements, such as the use of ExpressRoute to communicate with the enterprise's data-centers and/or on-premises networks, and private IPs for all but public end-points.
* **Monitoring and Analytics:** Capture telemetry to enable operations dashboards and automatic alerting of critical issues.
* **Service Authentication:** Allow only authorized access to services via token- or certificate-based service authentication.
* **Simulated Traffic:**
* **Chaos Testing:**

## OSS Technology Choices

Our team, Commercial Software Engineering (CSE), collaboratively codes with Microsoft's biggest and most important customers.
We see a huge spectrum of technology choices at different customers, ranging from all-Microsoft to all-OSS. Most comonly, we see a mix.

Given the wide range of technology choices out there, it's difficult to create a one-size-fits-all reference solution. For this particular project, we selected a set of technologies that are of interest to many of our customers.

This OSS solution uses the following OSS technologies:

* **GitHub:** Publishing this project to GitHub indicates our desire to share it widely and to encourage community contributions. 
* **Docker:** Though there are other container technologies out there, Docker/Moby is pretty much synonymous with the idea.
* **Java Version 8 (1.8.x):** A very common choice of programming langauages by many enterpises.
* **Spring Boot:** One of the most widely used and capable Java frameworks.
* **Spring Data REST:** A simple way to build REST APIs in a Spring Boot application that are backed by a persistent data repository.
* **Maven:** A commonly used tool for building and managing Java projects.
* **React:** Popular JavaScript framework for building UI. (Additional OSS tools used in the UI sample include TypeScript, webpack, and Jest.)

## Azure Technologies & Services

As with our OSS technology choices, we intentionally selected a set of Azure technologies and services that support common enterprise requirements, including:

* **Azure DevOps:** Microsoft's CI/CD solution, which is the Azure-branded version of Microsoft's mature and widely used VSTS solution.
* **Azure Resource Manager (ARM):** Azure's solution for deploying and managing Azure resources via JSON-based templates.
* **App Services:** A robust platform-as-a-service (PaaS) solution for application hosting. App Services hides the complexity of provisioning and managing VMs, auto-scaling, creating public IPs, etc.

>**Note:** App Services is appropriate for a wide range of enterprise apps, including certain highly scaled apps, though we often recommend 
>Azure Kubernetes Service (AKS) for apps that require certain advanced capabilities.

* **Cosmos DB:** Cosmos DB is perhaps the fastest and most reliable NoSQL data storage service in the world. It is an excellent choice when performance and reliability are a must, and when enterprises require multi-region write capabilities, which are essential for both application/service performance and for HA/DR scenarios.
* **Azure Traffic Manager:** DNS-based routing service to connect users to the nearest data center. Redirects traffic to healthy location when another region goes offline. Also enables recommended method blue-green (aka canary) deployments with Azure App Services.
* **Application Gateway:** Provides a single public end-point (public IP) and acts as a reverse proxy (based on URI path) to send requests to the correct App Service instance.
* **App Insights:** Enterprise developers use App Insights to monitor and detect performance anomolies in production applications.

The solution leverages Azure Dev Ops for Continuous Integration 
and Delivery (CI/CD), and it deploys complete Azure environments via Azure Resource Manager (ARM) templates.

## Key Benefits

Key technologies and concepts demonstrated:

| Benefit | Supporting Solution
|---|---
| Common, standard technologies | <li>Java programming language<li>Spring Boot Framework, one of the most widely used frameworks for Java<li>MongoDB NoSQL API (via Azure Cosmos DB)<li>Redis Cache
| Containerization | Microservices implemented in Docker containers, hosted by the Azure App Service for Containers PaaS service.
| CI/CD pipeline | Continuous integration/continuous delivery (CI/CD) is implemented using Azure DevOps with a pipeline of environments that support dev, testing and production
| Automated deployment | <li>Azure ARM templates<li>App Service for Containers<li>Azure container registry
| High Availability/Disaster Recovery (HA/DR) | Full geo-replication of microservices and data, with automatic failover in the event of an issue in any region:<br><br><li>Cosmos DB deployed to multiple regions with active-active read/write<li>Session consistency to assure that user experience is consistent across failover<li>Stateless microservices deployed to multiple regions<li>Health monitoring to detect errors that require failover<li>Azure Traffic Manager redirects traffic to healthy region
| Demonstrates insfrastructure best practices | <li>Application auto-scaling<li>Minimize network latency through geo-based DNS routing<li>API authentication<li>Distributed denial of service (DDoS) protection & mitigation
| Load and performance testing | The solution includes an integrated traffic simulator to demonstrate that the solution auto-scales properly, maintaining application performance as scale increases
| Proves application resiliency through chaos testing | A Chaos Monkey-style solution to shut down different portions of the architecture in order to validate that resilience measures keep everything running in the event of any single failure

## Contribute

See [CONTRIBUTING.md](./CONTRIBUTING.md) for more information.
