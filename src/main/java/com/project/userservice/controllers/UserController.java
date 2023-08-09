package com.project.userservice.controllers;

import com.project.userservice.mappers.UserMapper;
import com.project.userservice.models.*;
import com.project.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    public ResponseEntity<PublicUser> createUser(@RequestBody DBUser newUser) {
        DBUser createdUser = userService.createUser(newUser);
        return ResponseEntity.ok(userMapper.entityToPublicModel(createdUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        // Call the UserService to delete the user by userId
        userService.deleteUser(userId);

        // Return a success response
        return ResponseEntity.ok("User deleted successfully");
    }

//    @PatchMapping("/{userId}")
//    public ResponseEntity<PublicUser> updateUser(@PathVariable Integer userId, @RequestBody DBUser updatedUser) {
//        DBUser user = userService.updateUser(userId, updatedUser);
//        return ResponseEntity.ok(userMapper.entityToPublicModel(user));
//    }

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        Page<DBUser> dbUsers = userService.getAllUsers(pageSize, pageNumber);
        UserListResponse response = UserListResponse.builder()
                .users(userMapper.entityToPublicModel(dbUsers.getContent()))
                .totalElements(dbUsers.getTotalElements())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/interviewers")
    public ResponseEntity<List<PublicUser>> getAllUsers() {
        List<DBUser> dbUsers = userService.getAllUsersWithoutPagination();
        return ResponseEntity.ok(userMapper.entityToPublicModel(dbUsers));
    }

    @PatchMapping("/{userId}/admin")
    public ResponseEntity<?> partialUpdateAdminUser(
            @PathVariable Integer userId,
            @RequestBody PublicUser publicUser) {
        try {
            userService.partialUpdateAdminUser(userId, publicUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> partialUpdateUser(
            @PathVariable Integer userId,
            @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            userService.partialUpdateUser(userId, updateUserRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
//        User user = userService.getUserById(userId);
//        return ResponseEntity.ok(user);
//    }
}
