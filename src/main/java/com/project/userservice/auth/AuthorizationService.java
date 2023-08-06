package com.project.userservice.auth;

import com.project.userservice.config.JwtService;
import com.project.userservice.models.DBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthorizationService {
    @Autowired
    private JwtService jwtService;

    public String generateJWTTokenForUser(DBUser user) {
        return jwtService.generateToken(new HashMap<>(), user.getEmail());
    }
}
