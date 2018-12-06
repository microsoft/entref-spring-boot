package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Constants.DB_PRINCIPAL_COLLECTION)
public class Principal {
    @Id
    private ObjectId id;
    public String tconst;
    public Integer ordering;
    public String nconst;
    public String category;
    public String job;
    public List<String> characters;
}
