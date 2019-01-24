#!/bin/bash
echo
echo "Getting IMDb data files (updated daily)..."
curl --remote-name-all https://datasets.imdbws.com/{name.basics.tsv.gz,title.basics.tsv.gz,title.principals.tsv.gz}
echo
echo "Unzipping the data files (overwrites any existing .tsv files)..."
gunzip -vf *.gz
echo
echo "Removing IMDb '\N' values..."
ed -s *.tsv <<< 's/\\N//g'
echo
echo "IMDb data files ready:"
ls *.tsv
echo
