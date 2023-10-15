package com.jwt.token.application.repository;

import com.jwt.token.application.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    Product findByCategory(String category);
    Product findByName(String name);
}