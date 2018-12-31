# CosmosDB

Project Jackson uses a CosmosDB instance enabled with the DocumentDB API and an Azure Container Registry to push and pull images.
These are global resources and should be deployed independently of application infrastructure.
Deploy your own using the button below, which uses the included ARM template.

[![Deploy to Azure](https://azuredeploy.net/deploybutton.png)](https://azuredeploy.net/?repository=https://github.com/Microsoft/containers-rest-cosmos-appservice-java/infrastructure/global-resources)

## Deploying Data

> Note: This is optional, but is a good way to injest some sample data without needing to manually create it.

In this section we'll explain how to populate the [CosmosDB](https://azure.microsoft.com/en-us/services/cosmos-db/) instance that was created above with sample data.

### Install Dependencies

* Install [the latest Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)
* Install DocumentDB Emulator:
  * https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator
* Open a [shell](https://en.wikipedia.org/wiki/Shell_%28computing%29) in the project root directory (the next steps depend on this)

### Set up your environment variables

- Locate the provisioned CosmosDB instance in the [Azure Portal](https://portal.azure.com)
- Open the Cosmos Connection String blade
- Open the `data/importdata.sh` file
- Make sure the Cosmos DB resource is already created as described above
- From Bash command line, run `load_env.sh`. This will write/load any needed variables to the `vars.env` file
  - `RESOURCE_GROUP` - the Azure resource group name
  - `COSMOSDB_NAME` - the CosmosDB collection name (which is case sensitive)
  - `COSMOSDB_PASSWORD` - the CosmosDB's password (needed for when you load the data into Cosmos)
- Load `vars.env` into your environment or the VM where the app is being built locally
  - `source vars.env`
  - or in your chosen IDE, set your environment variables within your project
- NB: there will also be a DB_NAME and DB_CONNSTR for the Spring application (see the database section below in Application Configuration)

### Prepare the command line

- Switch into the project `data` directory: `cd data`
- Log into Azure: `az login`
- If you have multiple subscriptions, confirm that the project subscription is active:

``` Bash
az account show
az account set --subscription <subscription name/ID>
```

### Import the sample IMDb data to Cosmos DB

- Open a Bash command line
- Download and prepare the required IMDb data files:

``` Bash
data/getdata.sh
```

- Before starting to import data make sure the step `Set up your environment variables` is completed.
- Import the data into Cosmos collections

``` Bash
data/importdata.sh
```

### TIP: Explore the data from the DocumentDB Emulator

- Copy the Cosmos DB connection string from the "Connection String" blade
- Install documentdb cli using command `npm install -g documentdb-cli`
- Start the documentDB CLI with this command: `documentdb -s https://abcdef.documents.azure.com -k u1d0wTrlTWPVoA== -d mydatabase -l mycollection`
- Begin executing documentDB commands, by starting with help command such as:
`documentdb> .help`
- To exit the cli use the '.quit' command:
`documentdb> .quit`
