package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.microsoft.cse.reference.spring.dal.config.Constants;

@Document(collection = Constants.DB_CART_COLLECTION)
public class Cart {

    private int id;
    public String cartId;
    public Person cartOwner;
    public Item[] items;

    //Pn>0
//    public int cartStatus;
//    public dateTime timeCreated;
//    public double computedTotal;
}
