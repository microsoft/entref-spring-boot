#!/bin/bash

# Executes all the commands required to create and import the data to CosmosDB
az login
./load_env.sh
source ./vars.env
./getdata.sh
./importdata.sh