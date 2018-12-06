package com.microsoft.cse.reference.spring.dal.controllers;

import com.microsoft.cse.reference.spring.dal.models.Principal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create a repository of Principal models (not externally mounted) to facilitate route generation
 * for model CRUD operations as well as custom queries
 */
@Repository
@RepositoryRestResource(exported = false)
public interface PrincipalRepository extends MongoRepository<Principal, String> {
    /**
     * Create a custom query for searching by tconst
     * @param tconst the tconst value
     * @return the principal(s)
     */
    List<Principal> findByTconst(@Param("tconst") String tconst);

    /**
     * Create a custom query for searching by nconst
     * @param nconst the nconst value
     * @return the principal(s)
     */
    List<Principal> findByNconst(@Param("nconst") String nconst);
}