package com.microsoft.cse.reference.spring.dal.models;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = Constants.DB_TITLE_COLLECTION)
public class Title {
    private ObjectId id;
    @Id
    public String tconst;
    public String titleType;
    public String primaryTitle;
    public String originalTitle;
    public Boolean isAdult;
    public Integer startYear;
    public Integer endYear;
    public Integer runtimeMinutes;
    public List<String> genres;
}
