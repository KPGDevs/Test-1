package com.jwt.token.application.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class UserModel {
    @Id
    private ObjectId id;
    private String username;
    private String password;
    @DocumentReference
    private List<Role> roles = new ArrayList<>();
}