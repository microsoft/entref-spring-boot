
# Project Jackson Infrastructure

This repository tracks various Project Jackson ARM templates.

## Global ARM Template

> Note: Regional resources depend on Global ARM resources via certain variables. Complete this deployment first, so those variables can be used below!

To deploy all the global resources, see [The Global Readme](./global-resources/README.md) which also includes details on populating a [CosmosDB](https://azure.microsoft.com/en-us/services/cosmos-db/) instance with test data. 

## Regional ARM Template

To deploy all the resources, the script deploy.sh can be used.
The below values are required as the script inputs:

1. Azure Subscription ID
2. Azure Resource Group (Add existing if any else create a new one)
3. Azure Deployment Location (eastus, westus, etc)
4. App-name: Application Name

Another way to deploy is to run one-click deploy for all resources using below Deploy to Azure:

[![Deploy to Azure](http://azuredeploy.net/deploybutton.png)](https://azuredeploy.net/)

Once the ACR is deployed using the above deployment method follow the below manual steps to set up CD pipeline:

1. Create a new variable group in Azure Pipeline Library
2. Create variable ACR_SERVER and set value to the server name which will be the output of your deployment (<application name>container.azurecr.io)
3. Get values of username and password from container using Azure Portal
4. Create variables ACR_USERNAME and ACR_PASSWORD and set it using the above values respectively.
5. Once you set the above variables, your deployment resources can now be used as part of your CD pipeline.

## Environments

- Different environments like Dev, QA, Staging and Production environments are created under the resource group for all the resources to be deployed using ARM Template.
- Policies can be created between each of the environments to promote builds from one environment to another based on the requirements of the customer.
- These policies can differ for each customer and product.
- Once the tests under Dev environment passes, they can be approved to run on the QA environment based on policies set for approvals on each. These policies can be set under Azure DevOps Release Pipeline.

## Redis Cache

- A Redis cache is part of the resources to enhance performance of queries.
- The capacity of the Redis cache can be changed in the ARM template anywhere between 1 to 6.
- One also has the option to enable or disable Non SSL port from the ARM template.
- Azure allows 3 different values for the sku viz. Basic, Standard and Premium having different costs for each.

## Auto Scaling

- The app service is enabled with auto scaling
- When the CPU used is above 70 percent, the app will automatically be scaled to add another compute instance and this can be done for up to a max of 5 instances which can be changed based on requirements.
- When the memory on an instance used is above 70 percent, the app will automatically be scaled to add another compute instance and this can be done for up to a max of 5 instances which can be changed based on requirements.
- The minimum number of instances is set to be 1 which means, if the memory used on an instance is less, the instances will be scaled down automatically.

## Performance Testing

How to performance test an App Service.

1. Navigate to the Azure Portal for your App Service
2. Under `Developer Tools` select `Performance Testing`
!['This image is of the performance testing menu item'](/images/perftest1.png)
3. Select `New`
!['This image is of new performance testing button'](/images/perftest2.png)
4. Name the new performance test and configure the settings appropriately
!['This image is of performance testing settings'](/images/perftest3.png)
5. Submit the test and after resources are automatically allocated it will run

After it completes, Azure will automatically generate graphs and charts for you to easily analyze the performance test.
