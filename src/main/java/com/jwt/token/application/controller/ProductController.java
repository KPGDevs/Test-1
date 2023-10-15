package com.jwt.token.application.controller;

import com.jwt.token.application.model.Product;
import com.jwt.token.application.service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ServiceNotFoundException;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public Product saveProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable ObjectId id){
        return productService.deleteProduct(id);
    }

    @PutMapping("/updateProduct/{id}")
    public Product updateProduct(@PathVariable ObjectId id, @RequestBody Product product) throws ServiceNotFoundException {
        return productService.updateProduct(id, product);
    }
}