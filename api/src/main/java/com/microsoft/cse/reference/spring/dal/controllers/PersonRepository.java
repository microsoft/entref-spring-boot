package com.microsoft.cse.reference.spring.dal.controllers;

import com.microsoft.cse.reference.spring.dal.models.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create a repository of Person models (mounted at /people) to facilitate route generation
 * for model CRUD operations as well as custom queries
 */
@Repository
@RepositoryRestResource(path="people")
public interface PersonRepository extends MongoRepository<Person, String> {
    /**
     * Create a custom query for searching by primaryName
     * @param primaryName the person primary name
     * @return the person(s)
     */
    List<Person> findByPrimaryName(@Param("primaryName") String primaryName);
}