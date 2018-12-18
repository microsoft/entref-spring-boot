#!/bin/bash

################################################################################
# Variables
################################################################################

resourceGroup=$RESOURCE_GROUP
cosmosName=$COSMOSDB_NAME
password=$COSMOSDB_PASSWORD
databaseName=jacksonDatabase
files=("name.basics.tsv" "Products.csv")
collections=(names products)
keys=("nconst" "NDBNumber")

len=${#collections[@]}

################################################################################
# Helpers
################################################################################
create_database() {
  az cosmosdb database create -g $resourceGroup -n $cosmosName --db-name $databaseName > /dev/null
}

create_collections() {
  for ((i=0; i<len; i++)); do
    step=$((i + 1))
    echo "($step of $len) Creating collection '${collections[i]}'"
    partition="/'\$v'/${keys[$i]}/'\$v'"
    az cosmosdb collection create -g $resourceGroup -n $cosmosName --db-name $databaseName --collection-name ${collections[$i]} \
      --partition-key-path $partition --throughput 1700 > /dev/null
  done
}


################################################################################
# Main script
################################################################################
set -e

echo
echo "Creating Cosmos DB database..."
create_database

echo
echo "Creating Cosmos DB collections..."
create_collections

echo
echo "Creation steps ... Complete!"
echo
