#!/bin/bash
echo
echo "Getting IMDb data files (updated daily)..."
curl --remote-name-all https://datasets.imdbws.com/{name.basics.tsv.gz,title.basics.tsv.gz,title.principals.tsv.gz}
echo
echo "Unzipping the data files (overwrites any existing .tsv files)..."
gunzip -vf *.gz
echo
echo "Removing IMDb '\N' values..."
sed --in-place 's/\\N//g' *.tsv
echo
echo "IMDb data files ready:"
ls *.tsv
echo 
