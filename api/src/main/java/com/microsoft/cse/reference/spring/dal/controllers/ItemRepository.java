package com.microsoft.cse.reference.spring.dal.controllers;

import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.microsoft.cse.reference.spring.dal.models.Item;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path="items")
public interface ItemRepository extends DocumentDbRepository<Item, String> {
}
