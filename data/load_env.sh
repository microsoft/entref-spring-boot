#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

# -e: immediately exit if any command has a non-zero exit status
# -o: prevents errors in a pipeline from being masked
# IFS new value is less likely to cause confusing bugs when looping arrays or arguments (e.g. $@)

#******************************************************************************
# Script to set environment variables
#******************************************************************************

usage() { echo "Usage: $0 -i <subscriptionId> -g <resourceGroupName> -n <appName> -l <resourceGroupLocation>" 1>&2; exit 1; }

declare subscriptionId=""
declare resourceGroupName=""
declare resourceGroupLocation=""
declare dbName=""
declare connString=""
declare dbPassword=""

# Initialize parameters specified from command line
while getopts ":i:g:n:l:" arg; do
    case "${arg}" in
        i)
            subscriptionId=${OPTARG}
        ;;
        g)
            resourceGroupName=${OPTARG}
        ;;
        l)
            resourceGroupLocation=${OPTARG}
        ;;
        d)
            dbName=${OPTARG}
    esac
done
shift $((OPTIND-1))

#******************************************************************************
# Helper functions
#******************************************************************************

azLogin() {
    (
        set +e
        # Login to azure using your credentials
        az account show &> /dev/null

        if [ $? != 0 ];
        then
            echo "Azure login required..."
            az login -o table
        else
            az account list -o table
        fi
    )
}

validatedRead() {
    # Take a prompt for the user, the regex pattern and the error to return if the input fails to pattern-match
    prompt=$1
    regex=$2
    error=$3

    userInput=""
    while [[ ! ${userInput} =~ ${regex} ]]; do
        if [[ (-n ${userInput}) ]]; then
            printf "'%s' is not valid. %s\n" ${userInput} ${error}
        fi
        read -p "${prompt}" userInput
    done
}

readSubscriptionId() {
    # Display the subscriptions associated with the User. User picks, and we validate that choice

    currentSub="$(az account show -o tsv | cut -f2)"
    subNames="$(az account list -o tsv | cut -f4)"
    subIds="$(az account list -o tsv | cut -f2)"

    while ([[ -z "${subscriptionId}" ]]); do
        printf "Enter your subscription ID [%s]: " "${currentSub}"
        read userInput

        if [[ (-z "${userInput}") && (-n "${currentSub}")]]; then
            userInput=${currentSub}
        fi

        set +e
        nameExists="$(echo "${subNames}" | grep "${userInput}")"
        idExists="$(echo "${subIds}" | grep "${userInput}")"

        if [[ (-z "${nameExists}") && (-z "${idExists}") ]]; then
            printf "'%s' is not a valid subscription name or ID.\n" "${userInput}"
        else
            subscriptionId=${userInput}
            printf "Using subscription '%s'...\n" "${subscriptionId}"
        fi
    done
}

readResourceGroupName() {
    # Display a list of resource groups available to the signed in user
    # User picks, and we validate that choice

    printf "Existing resource groups:\n"
    groups="$(az group list -o tsv | cut -f4 | tr '\n' ', ' | sed "s/,/, /g")"
    printf "\n%s\n" "${groups%??}"
    validatedRead "Enter a resource group name: " "^[a-zA-Z0-9_-]+$" "Only letters, numbers and underscores are allowed."
    resourceGroupName=${userInput}

    set +e

    # Check for existing RG
    az group exists --name "${resourceGroupName}" &> /dev/null
    if [ "$?" == "false" ]; then
        echo "To create a new resource group, please enter an Azure location:"
        readLocation

        (set -ex; az group create --name "${resourceGroupName}" --location "${resourceGroupLocation}")
    else
        resourceGroupLocation="$(az group show -n "${resourceGroupName}" -o tsv | cut -f2)"
        printf "Using resource group '%s'...\n" "${resourceGroupName}"
    fi

    set -e
}

readLocation() {
    # For use when creating a new database
    # Displays a list of available database replication origins under the resource group
    # User picks, then we validate that choice

    if [[ -z "${resourceGroupLocation}" ]]; then
        locations="$(az account list-locations --output tsv | cut -f5 | tr '\n' ', ' | sed "s/,/, /g")"
        printf "\n%s\n" "${locations%??}"

        declare locationExists
        while ([[ -z ${resourceGroupLocation} ]]); do
            validatedRead "Enter resource group location: " "^[a-zA-Z0-9]+$" "Only letters & numbers are allowed."
            locationExists="$(echo "${locations}" | grep "${userInput}")"
            if [[ -z ${locationExists} ]]; then
                printf "'%s' is not a valid location.\n" "${userInput}"
            else
                resourceGroupLocation=${userInput}
                printf "Using resource group '%s'...\n" "${resourceGroupName}"
            fi
        done
    fi
}

readDbName() {
    # Displays the list of available databases under the previously specified resource group
    # User then picks their desired and we validate that choice (in case of typo, etc.)

    dbNames="$(az cosmosdb list -g ${resourceGroupName} -o tsv | cut -f13)"
    defaultDb=(${dbNames[@]})

    while ([[ -z "${dbName}" ]]); do
        printf "Cosmos DB instances in group '%s':\n\n" "${resourceGroupName}"
        dbNames="$(az cosmosdb list -g "${resourceGroupName}" -o tsv | cut -f13 | tr '\n' ', ' | sed "s/,/, /g")"
        printf "%s\n\n" "${dbNames%??}"

        printf "Enter the Cosmos DB name [%s]: " "${defaultDb}"
        read userInput

        if [[ (-z "${userInput}") && (-n "${defaultDb}")]]; then
            userInput=${defaultDb}
        fi

        set +e
        nameExists="$(az cosmosdb check-name-exists -n "${userInput}")"

        if [[ "${nameExists}" == "false" ]]; then
            printf "'%s' is not a valid Cosmos DB name.\n" "${userInput}"
        else
            dbName=${userInput}
            printf "Using database '%s'...\n" "${dbName}"

        fi
    done
}

#******************************************************************************
# Script to set environment variables
#******************************************************************************

azLogin

# Prompt for parameters if some required parameters are missing
if [[ -z "${subscriptionId}" ]]; then
    echo
    readSubscriptionId
fi

# Set the default subscription id
az account set --subscription "${subscriptionId}"

if [[ -z "${resourceGroupName}" ]]; then
    echo
    readResourceGroupName
fi

if [[ -z "${dbName}" ]]; then
    echo
    readDbName
fi

# At this time, list-connection-strings does not support '-o tsv', so this command uses sed to extract the connection string from json results
connString="$(az cosmosdb list-connection-strings --name "${dbName}" -g "${resourceGroupName}" | sed -n -e '4 p' | sed -E -e 's/.*mongo(.*)true.*/mongo\1true/')"
# But list-keys does support `-o tsv`
dbPassword="$(az cosmosdb list-keys --resource-group "${resourceGroupName}" --name "${dbName}" -o tsv | sed -e 's/\s.*$//')"

touch vars.env
echo "export RESOURCE_GROUP=${resourceGroupName}" > vars.env
echo "export COSMOSDB_NAME=${dbName}" >> vars.env
echo "export COSMOSDB_PASSWORD=${dbPassword}" >> vars.env
# Creates a distinction - some of these ENV variables will be used exclusively to connect to an Azure CosmosDB instance,
#   but in an Azure-agnostic setup, the DB_NAME and DB_CONNSTR may not be on CosmosDB
echo "export DB_NAME=${dbName}" >> vars.env
echo "export DB_CONNSTR=${connString}" >> vars.env
printf "\nVariables written to file 'vars.env'\n"

