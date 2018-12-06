package com.microsoft.cse.reference.spring.dal.controllers;

import com.microsoft.cse.reference.spring.dal.models.Title;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create a repository of Title models (mounted at /titles) to facilitate route generation
 * for model CRUD operations as well as custom queries
 */
@Repository
@RepositoryRestResource(path = "titles")
public interface TitleRepository extends MongoRepository<Title, String> {
    /**
     * Create a custom query for searching by primaryTitle
     * @param primaryTitle the title primary title
     * @return the title(s)
     */
    List<Title> findByPrimaryTitle(@Param("primaryTitle") String primaryTitle);
}