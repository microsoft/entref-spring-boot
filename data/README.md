# Data

## Deploying Data
This project uses CosmosDB to store our sample data.  CosmosDB is a global resource that is provisioned using the Global Resources in this [directory]( https://github.com/Microsoft/containers-rest-cosmos-appservice-java/tree/master/infrastructure/global-resources).

## Creating and Importing sample data in CosmosDB.

A Dockerfile has also been included in this directory to simplify the upload of the sample data without needing to install dependencies locally in your system.

1. Build the docker image: 
``` bash 
$ docker build -t cosmos-import  .
```

2.  Either run the docker image: 
``` bash
$ docker run -it cosmos-import bash
```

3.  Or run your local data directory into the container (helpful for testing changes on the script) use:
```
$ docker run -it --mount src="$(pwd)",target=/containers-rest-cosmos-appservice-java/data,type=bind cosmos-import bash
```

4. Execute the all-in-on import script inside the running container:  
``` bash
chmod +x cosmos-import.sh
./cosmos-import.sh
``` 