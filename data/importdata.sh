#!/bin/bash
set -e

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
confirm_locations() {
    # If the above key variables are empty, exit and suggest rerunning load_env.sh
    if [[ -z "${resourceGroup}" ]] || [[ -z "${cosmosName}" ]] || [[ -z "${password}" ]]; then
        echo "There are no set environment variables."
        echo "Please (re)run load_env.sh"
        exit 1
    fi

    # Check if the resource group is correctly set
    printf "Is this the correct resource group name? [%s] Y,N (leave blank for Y): " "${resourceGroup}"
    read userInput

    if [[ -z "${userInput}" ]] || [[ "${userInput}" =~ "Y" ]] || [[ "${userInput}" == "y" ]]; then
        echo "Resource group confirmed."
    elif [[ "${userInput}" == "N" ]] || [[ "${userInput}" == "n" ]]; then
        printf "\tPlease enter the correct resource group: "
        read userInput
        resourceGroup=${userInput}

        # Check to see if the provided resource group actually exists
        echo "Confirming whether the resource group '${resourceGroup}' exists..."
        resGroupExists="$(az cosmosdb check-name-exists -n "${resourceGroup}")"
        if [[ "${resGroupExists}" == "false" ]]; then
            echo "Exiting. Provided resource name does not exist. Rerun script."
            exit 1
        fi
    else
        echo "Exiting. Rerun this script or load_env.sh for best results."
        exit 1
    fi

    # Check if the CosmosDB account name is correctly set
    # If the account name is incorrect, then prompting for the access key follows up
    printf "Is this the correct CosmosDB account name? [%s] Y,N (leave blank for Y): " "${cosmosName}"
    read userInput

    if [[ -z "${userInput}" ]] || [[ "${userInput}" == "Y" ]] || [[ "${userInput}" == "y" ]]; then
        echo "Account name confirmed."
    elif [[ "${userInput}" == "N" ]] || [[ "${userInput}" == "n" ]]; then
        printf "\tPlease enter the correct CosmosDB account name: "
        read userInput
        cosmosName=${userInput}
        printf "\tPlease enter the associated primary key: "
        read userInput
        password="${userInput}"

        # Check to see if the provided cosmosName actually exists
        echo "Confirming whether the account '${cosmosName}' exists..."
        accountExists="$(az cosmosdb check-name-exists -n "${cosmosName}")"
        if [[ "${accountExists}" == "false" ]]; then
            echo "Exiting. Provided account name does not exist. Rerun script."
            exit 1
        fi
    else
        echo "Exiting. Rerun this script or load_env.sh for best results."
        exit 1
    fi
}

create_database() {
    printf "\n"
    echo "Checking whether the database '${databaseName}' already exists..."

    # Check to see if the database we're creating exists already
    dbExists="$(az cosmosdb database exists --db-name "${databaseName}" -n "${cosmosName}" --key "${password}")"

    if [[ "${dbExists}" = "false" ]]; then
        echo "Creating database '${databaseName}'..."

        # Try to create the database
        az cosmosdb database create -g "${resourceGroup}" -n "${cosmosName}" --db-name "${databaseName}" > /dev/null
        if [ "$?" != "0" ]; then
            # Catch any errors if the creation failed
            echo "Error when creating the database" 1>&2
            exit 1
        fi

        echo "Database '${databaseName}' has been created."
    else
        echo "Database '${databaseName}' already exists. Moving on..."
    fi
}

create_collections() {
    # Go through each collection and see whether it exists already
    for ((i=0; i<len; i++)); do
        step=$((i + 1))
        echo "Checking whether the collection '${collections[i]}' exists..."

        collExists="$(az cosmosdb collection exists -c "${collections[i]}" -d "${databaseName}" -n "${cosmosName}" -g "${resourceGroup}")"

        if [[ "${collExists}" = "false" ]]; then
            echo "(${step} of ${len}) Creating collection '${collections[i]}'"

            # Create the collections with a high throughput since we'll be uploading a lot of data
            partition="/'\$v'/${keys[$i]}/'\$v'"
            az cosmosdb collection create -g "${resourceGroup}" -n "${cosmosName}" --db-name "${databaseName}" --collection-name "${collections[$i]}" \
                --partition-key-path "${partition}" --throughput 100000 > /dev/null

            if [ "$?" != "0" ]; then
                echo "Error when creating the collection '${collections[$i]}'" 1>&2
                exit 1
            fi

            echo "Collection '${collections[i]}' has been created."
        else
            echo "Collection '${collections[i]}' already exists. Moving on..."
        fi
    done
}

delete_tsv_files() {
    # Clean up the download TSVs
    for ((i=0; i<len; i++)); do
        rm -v "${files[$i]}"
    done
}

import_data() {
    for ((i=0; i<len; i++)); do
        # For each file upload the data using the MongoImport Tool
        step=$((i + 1))
        echo
        echo "(${step} of ${len}) Importing data from file '${files[$i]}' to collection '${collections[$i]}'..."

        hostName="${cosmosName}.documents.azure.com:10255"
        user=${cosmosName}

        mongoimport --host "${hostName}" -u "${user}" -p "${password}" --ssl --sslAllowInvalidCertificates --type tsv --headerline \
        --db "${databaseName}" --collection "${collections[$i]}" --numInsertionWorkers 40 --file "${files[$i]}"

        if [ "$?" != "0" ]; then
            echo "Error while importing from file '${files[$i]}'" 1>&2
            exit 1
        fi

        echo
        echo "Import to '${collections[$i]}' is complete"
    done
}

set_throughput() {
    # Once we're done, throttle back the throughput to the low side - you can always increase it later
    RUs=1700
    for ((i=0; i<len; i++)); do
        echo
        echo "Setting '${collections[$i]}' throughput to ${RUs} to reduce cost..."
        az cosmosdb collection update -g "${resourceGroup}" -n "${cosmosName}" --db-name "${databaseName}" --collection-name "${collections[$i]}" --throughput "${RUs}"

        if [ "$?" != "0" ]; then
            echo "Error while updating the throughput on collection '${collections[$i]}'" 1>&2
            exit 1
        fi
    done
}

################################################################################
# Main script
################################################################################
set -e

printf "\nStep 1. Confirming locations...\n"
confirm_locations

printf "\nStep 2. Creating Cosmos DB database...\n"
create_database

printf "\nStep 3. Creating Cosmos DB collections...\n"
create_collections

printf "Step 4. Importing IMDb data to Cosmos DB...\n"
import_data

printf "Step 5. Finished importing data. Cleaning up...\n"
delete_tsv_files

printf "\nStep 6. Reducing throughput on Azure...\n"
set_throughput
printf "This may take 10 minutes to be reflected in the portal."

printf "\nComplete!"

