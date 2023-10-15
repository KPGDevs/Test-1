package com.jwt.token.application.service;

import com.jwt.token.application.error.ProductNotFoundException;
import com.jwt.token.application.model.Product;
import com.jwt.token.application.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public ResponseEntity<?> deleteProduct(ObjectId id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found by id: " + id);
    }

    public Product updateProduct(ObjectId id, Product product) {
        if(productRepository.existsById(id)) {
            Optional<Product> productFromMongo = productRepository.findById(id);
            Product prod = productFromMongo.get();
            if (product.getName() != null)
                prod.setName(product.getName());
            if (product.getCategory() != null)
                prod.setCategory(product.getCategory());
            if (product.getPrice() > 0)
                prod.setPrice(product.getPrice());
            if (product.getImageUrl() != null)
                prod.setImageUrl(product.getImageUrl());
            return productRepository.save(prod);
        } else {
            throw new ProductNotFoundException("Product not found");
        }
    }

}