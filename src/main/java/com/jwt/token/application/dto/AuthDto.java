package com.jwt.token.application.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String username;
    private String password;
}