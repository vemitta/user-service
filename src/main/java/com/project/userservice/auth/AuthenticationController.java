package com.project.userservice.auth;

import com.project.userservice.models.DBUser;
import com.project.userservice.models.User;
import com.project.userservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        DBUser createdUser = authenticationService.register(request);

        // Generate the JWT token for the created user
        String token = authorizationService.generateJWTTokenForUser(createdUser);

        // Set the token using the setToken method of DBUser
        createdUser.setToken(token);

        // Save the updated user with the token
        userService.updateUser(createdUser.getId(), createdUser);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) {
        DBUser existingUser = authenticationService.authenticate(request);

        // Generate the JWT token for the created user
        String token = authorizationService.generateJWTTokenForUser(existingUser);

        // Set the token using the setToken method of DBUser
        existingUser.setToken(token);

        // Save the updated user with the token
        userService.updateUser(existingUser.getId(), existingUser);

        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Call the logout service method to perform the logout logic
            authenticationService.logout(request, response);
            return ResponseEntity.ok("Logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to logout.");
        }
    }
}
