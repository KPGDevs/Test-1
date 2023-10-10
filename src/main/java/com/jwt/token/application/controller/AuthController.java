package com.jwt.token.application.controller;

import com.jwt.token.application.dto.AuthDto;
import com.jwt.token.application.dto.AuthResponseDTO;
import com.jwt.token.application.model.Role;
import com.jwt.token.application.model.UserModel;
import com.jwt.token.application.repository.RoleRepository;
import com.jwt.token.application.repository.UserRepository;
import com.jwt.token.application.security.JWTGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDto registerDto, HttpServletResponse response) {
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username is taken");
        }

        UserModel user = new UserModel();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findByName("USER").orElse(null);
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
        ResponseEntity<AuthResponseDTO> tokenResponse = login(registerDto);

        if (tokenResponse != null && tokenResponse.getBody() != null) {
            String token = tokenResponse.getBody().getAccessToken();
            Cookie cookie = new Cookie("jwtToken", token);

            cookie.setPath("/");
            cookie.setMaxAge(7200);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);

            response.addCookie(cookie);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        SecurityContextHolder.clearContext();

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}