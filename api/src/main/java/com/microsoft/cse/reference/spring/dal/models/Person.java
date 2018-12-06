package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Constants.DB_PERSON_COLLECTION)
public class Person {
    private ObjectId id;
    @Id
    public String nconst;
    public String primaryName;
    public Integer birthYear;
    public Integer deathYear;
    public List<String> primaryProfession;
    public List<String> knownForTitles;
}
