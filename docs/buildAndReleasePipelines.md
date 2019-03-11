# Build and Release Pipelines

For Project Jackson, the team utilized [Azure Dev Ops](https://azure.microsoft.com/en-us/services/devops/) for source control, work tracking, build and release pipelines. The build pipelines were set up for the API and for the small client application. The build artifacts were used as the kickoff point for the release pipelines for each of the deployment pipelines.

## API Build

The API utilizes Spring Boot to create a standalone Java application. The API is described in depth in [the swagger doc](../swagger.yml). The API uses [Maven](https://maven.apache.org/) with [Spring Boot](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/) to test, compile, run and package the self-contained executable jar that runs in production. To create the executable jar, a dependency in `pom.xml` is added:

```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
Once this is added, the command `mvn package` can be run, which will produce a `example-name.jar` which can be run with the command `java -jar example-name.jar`. 

To build the Project Jackson API repo in Azure Dev Ops, a build pipeline was created and described with the [`azure-pipelines.yml`](../azure-pipelines.yml) file. It is broken up into sections based on build step. It starts by running `mvn test` which runs all the unit and integration tests and if that step is successful, it runs `mvn package` and creates that executable jar described previously. 

The next steps involve building the docker image defined in the [`Dockerfile`](../Dockerfile), tagging it with the Azure Container Registry (ACR) name and build number, and pushing the tagged Docker image to ACR. The variables for ACR such as `$ACR_SERVER`, `$ACR_CONTAINER_TAG`, `$ACR_PASSWORD`, and `$ACR_USERNAME` are defined in Azure Dev Ops under the build for this repo. The team created a variable group in Azure Dev Ops, under Pipelines, then Library, called `ACR Credentials` that contains the `ACR_SERVER`, `ACR_USERNAME`, and `ACR_PASSWORD` variables. Then in the build pipeline for the API repository, there is a `Variables` tab that has `Variable Groups` as an option and that is where the `ACR Credentials` variable group is linked to the build pipeline for the repository. The `$ACR_CONTAINER_TAG` is set in the `Variables` tab as well, but under `Pipeline Variables` and the team uses a value of `pj-api/pj-api-combined:$(Build.BuildNumber)`.  

## Client Build

The UI is a React and TypeScript web app that allows users to easily test the API endpoints and see the data that is returned from the database in a cleaner way. The build pipeline for it was also setup with an `azure-pipelines.yml` file, broken into npm and docker steps. The npm steps include linting, testing and building through the following commands.
```
npm run lint
npm run test
npm run build
```

After those steps finish successfully, a docker image is built, tagged, and pushed into ACR in the same way that the API container is dockerized. The difference is that the `$ACR_CONTAINER_TAG` variable in the UI build `Variables` is `pj-ui:$(Build.BuildNumber)` to differentiate the docker container from the API in ACR.


## API Release Pipeline

The release pipeline for the API was built with Azure Dev Ops, under `Pipelines` and `Releases`. The pipeline was configured so that when there was a new build of the master branch in the API repository, a deployment of the API to Azure App Services would start. The API Deployment pipeline has two tasks in the `Stages` section, which are `Person Endpoint Deploy` and `Title Endpoint Deploy`. Each task deploys an Azure App Service of the type `Linux App` to the `jackson-person` and `jackson-title`, respectively, App Services. The `Image Source` is `Container Registry`, the `Registry or Namespace` is `jacksoncontainer.azurecr.io`, the `Repository` is `pj-api/pj-api-combined`, and the `Tag` is `$(Build.BuildNumber)` which match the values for the docker image pushed into ACR that was set in the build step. 

## Client Release Pipeline

Like the build pipeline, the release pipeline for the UI is almost identical to the API release pipeline, with key differences being that only the `pj-client` App Service is deployed to and that the docker image being pulled from ACR is from the `pj-client` repository. Only a single environment variable is defined in `Application settings` and it is named `WEBSITES_PORT` with a value of `8080`. In a similar fashion to the API, when there is a build on the master branch of the UI repository, a new release deployment is kicked off.


### Walkthrough

A walkthrough release pipeline video can be found [here](https://msit.microsoftstream.com/video/9dab7948-d167-4ec1-b210-bc07f81a1d25).