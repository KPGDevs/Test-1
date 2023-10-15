package com.jwt.token.application.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
public class Product {
    @Id
    private ObjectId id;
    private String name;
    private String category;
    private double price;
    private String imageUrl;
}