#!/bin/bash
echo
echo "Getting IMDb data files (updated daily)..."
curl --remote-name-all https://datasets.imdbws.com/name.basics.tsv.gz
echo
echo "Unzipping the data files (overwrites any existing .tsv files)..."
gunzip -vf *.gz
echo
echo "Removing IMDb '\N' values..."
sed --in-place 's/\\N//g' *.tsv
echo
echo
echo "Grabbing Product Data..."
curl --output products.zip https://www.ars.usda.gov/ARSUserFiles/80400525/Data/BFPDB/BFPD_csv_07132018.zip
echo "Unzipping the data file and removing the extraneous files..."
unzip *.zip
rm -f Derivation_Code_Description.csv Nutrients.csv Serving_size.csv BFPD_Doc.pdf
echo "Removing underscores from data..."
sed --in-place 's/_[a-zA-Z]/\U&\E/g' *.csv
sed --in-place 's/_//g' *.csv
rm -f sed* products.zip
echo "Data files ready!"
echo "Next steps require an editor like Excel to make mass changes to the CSV and TSV"
