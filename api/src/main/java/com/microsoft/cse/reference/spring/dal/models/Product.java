package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import com.microsoft.cse.reference.spring.dal.config.Constants;

@Document(collection = Constants.DB_PRODUCT_COLLECTION)
public class Product {
    private int id;
    public String NDBNumber;
    public String longName;
    public String dataSource;
    public String gtinUpc;
    public String manufacturer;
    public String dateModified;
    public String dateAvailable;
    public String ingredientsEnglish;
}
