#!/bin/bash
printf "\nRunning the getdata.sh script.\n"

# Accesses the IMDb data that gets aggregrated daily
read -p "Confirm running by hitting the [enter] key" userInput
if [[ -z "${userInput}" ]]; then
    echo "Getting IMDb data files (updated daily)..."
    curl --remote-name-all https://datasets.imdbws.com/{name.basics.tsv.gz,title.basics.tsv.gz,title.principals.tsv.gz} || ( echo "Failed to access data sets. Exiting" && exit 1 )
fi

# Unzips the compressed downloads
read -p "Press the [enter] key to continue" userInput
if [[ -z "${userInput}" ]]; then
    echo "Unzipping the data files (overwrites any existing .tsv files)..."
    gunzip -vf *.gz || ( echo "Failed to unzip data sets. Exiting" && exit 1 )
fi

# Scrubs bad values from the data set
read -p "Press the [enter] key to continue" userInput
if [[ -z "${userInput}" ]]; then
    printf "\n"
    echo "Removing IMDb '\N' values..."
    ed -s *.tsv <<< $',s|\\\N||g\nw' || ( echo "Failed to fix bad values in data sets. Exiting" && exit 1 )
fi

# Shows the user the files
printf "\n"
echo "IMDb data files ready:"
ls *.tsv
echo
