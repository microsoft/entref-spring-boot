package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.microsoft.cse.reference.spring.dal.config.Constants;

import org.springframework.data.annotation.Id;


import java.util.List;

@Document(collection = Constants.DB_PERSON_COLLECTION)
public class Person {
    private int id;
    public String nconst;
    public String primaryName;
    public Integer birthYear;
    public Integer deathYear;
    public List<String> primaryProfession;
    public List<String> knownForTitles;
}
