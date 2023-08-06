package com.project.userservice.auth;

import com.project.userservice.config.JwtService;
import com.project.userservice.exception.UserRegistrationException;
import com.project.userservice.models.DBUser;
import com.project.userservice.repositories.UserRepository;
import com.project.userservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final UserService userService;


    public DBUser register(RegisterRequest request) {
        // Check if user with the given email already exists
        Optional<DBUser> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserRegistrationException("Email already exists");
        }

        var user = DBUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .token(null)
                .skills(null)
                .build();

        return userRepository.save(user);
    }

    public DBUser authenticate(AuthenticationRequest request) {
        DBUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractSubject(jwt);
        if (userEmail != null) {
            // Fetch the user from the database using the email
            DBUser user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

            // Update the user's token to null
            user.setToken(null);
            userService.updateUser(user.getId(), user);
        }
        response.setHeader("Authorization", "");
    }
}

