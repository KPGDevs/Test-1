package com.jwt.token.application.repository;

import com.jwt.token.application.model.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, ObjectId> {
    Optional<UserModel> findByUsername(String username);
    Boolean existsByUsername(String username);
}