#!/bin/bash

################################################################################
# Variables
################################################################################

resourceGroup=$RESOURCE_GROUP
cosmosName=$COSMOSDB_NAME
password=$COSMOSDB_PASSWORD
databaseName=IMDb
files=("title.basics.tsv" "name.basics.tsv" "title.principals.tsv")
collections=(titles names principals_mapping)
keys=("tconst" "nconst" "tconst")

len=${#collections[@]}

################################################################################
# Helpers
################################################################################
create_database() {
  echo "Checking whether the database '${databaseName}' already exists..."

  dbExists="$(az cosmosdb database exists --db-name ${databaseName} -n ${cosmosName} --key ${password})"

  if [[ "${dbExists}" = "false" ]]; then
    echo "Creating database '${databaseName}'..."

    az cosmosdb database create -g ${resourceGroup} -n ${cosmosName} --db-name ${databaseName} > /dev/null

    echo "Database '${databaseName}' has been created."
  else
    echo "Database '${databaseName}' already exists. Moving on..."
  fi
}

create_collections() {

  for ((i=0; i<len; i++)); do
    step=$((i + 1))
    echo "Checking whether the collection '${collections[i]}' exists..."

    collExists="$(az cosmosdb collection exists -c ${collections[i]} -d ${databaseName} -n ${cosmosName} -g ${resourceGroup})"

    if [[ "${collExists}" = "false" ]]; then
      echo "(${step} of ${len}) Creating collection '${collections[i]}'"

      partition="/'\$v'/${keys[$i]}/'\$v'"
      az cosmosdb collection create -g ${resourceGroup} -n ${cosmosName} --db-name ${databaseName} --collection-name ${collections[$i]} \
      --partition-key-path ${partition} --throughput 100000 > /dev/null

      echo "Collection '${collections[i]}' has been created."
    else
      echo "Collection '${collections[i]}'' already exists. Moving on..."
    fi
  done
}

delete_tsv_files() {
  for ((i=0; i<len; i++)); do
    rm -v ${files[$i]}
  done
}

import_data() {
  for ((i=0; i<len; i++)); do
    step=$((i + 1))
    echo
    echo "(${step} of ${len}) Importing data from file '${files[$i]}' to collection '${collections[$i]}'..."

    hostName="${cosmosName}.documents.azure.com:10255"
    user=${cosmosName}

    mongoimport --host ${hostName} -u ${user} -p ${password} --ssl --sslAllowInvalidCertificates --type tsv --headerline \
      --db ${databaseName} --collection ${collections[$i]} --numInsertionWorkers 40 --file ${files[$i]}

    echo
    echo "Import to '${collections[$i]}' is complete"
  done
}

set_throughput() {
  RUs=1700
  for ((i=0; i<len; i++)); do
    echo
    echo "Setting '${collections[$i]}' throughput to ${RUs} to reduce cost..."
    az cosmosdb collection update -g ${resourceGroup} -n ${cosmosName} --db-name ${databaseName} --collection-name ${collections[$i]} --throughput ${RUs}
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
echo "Importing IMDb data to Cosmos DB..."
import_data

echo
echo "Finished importing data. Cleaning up..."
delete_tsv_files

echo
echo "Reducing throughput on Azure..."
set_throughput
echo "This may take 10 minutes to be reflected in the portal."

echo
echo "Complete!"
echo
