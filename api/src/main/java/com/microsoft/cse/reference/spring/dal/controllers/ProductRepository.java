package com.microsoft.cse.reference.spring.dal.controllers;

import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.microsoft.cse.reference.spring.dal.models.Product;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path="products")
public interface ProductRepository extends DocumentDbRepository<Product, String> {
}
