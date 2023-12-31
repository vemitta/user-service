package com.project.userservice.services;

import com.project.userservice.models.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
//    User saveUser(User user);
    Optional<DBUser> getUser(String email);

    void deleteUser(Integer userId);

    DBUser updateUser(Integer userId, DBUser updatedUser);

    Page<DBUser> getAllUsers(Integer pageSize, Integer pageNumber);

    Optional<DBUser> getUserById(Integer userId);

    DBUser createUser(DBUser newUser);

    List<DBUser> getAllUsersWithoutPagination();

    void partialUpdateUser(Integer userId, UpdateUserRequest updateUserRequest);

    void partialUpdateAdminUser(Integer userId, PublicUser publicUser);
}
