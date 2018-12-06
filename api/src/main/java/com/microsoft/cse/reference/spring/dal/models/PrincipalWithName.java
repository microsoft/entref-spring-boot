package com.microsoft.cse.reference.spring.dal.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.LinkedHashMap;
import java.util.List;

public class PrincipalWithName {
    @Id
    private ObjectId id;
    public String tconst;
    public Integer ordering;
    public LinkedHashMap<?,?> person;
    public String category;
    public String job;
    public List<String> characters;
}
