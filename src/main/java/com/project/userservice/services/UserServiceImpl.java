package com.project.userservice.services;

import com.project.userservice.exception.ResourceNotFoundException;
import com.project.userservice.models.DBUser;
import com.project.userservice.models.User;
import com.project.userservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<DBUser> getUser(String email) {
        log.info("Fetching user {}", email);
        Optional<DBUser> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return userOptional;
    }

    @Override
    public void deleteUser(Integer userId) {
        // Retrieve the user by userId
        Optional<DBUser> optionalUser = userRepository.findById(userId);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            // Delete the user
            DBUser user = optionalUser.get();
            userRepository.delete(user);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

//    @Override
//    public DBUser updateUser(Integer userId, DBUser updatedUser) {
//        Optional<DBUser> optionalUser = userRepository.findById(userId);
//
//        if (optionalUser.isPresent()) {
//            DBUser existingUser = optionalUser.get();
//
//            // Update the user details
//            existingUser.setName(updatedUser.getName());
//            existingUser.setEmail(updatedUser.getEmail());
//            existingUser.setPassword(updatedUser.getPassword());
//
//            // Save the updated user
//            return userRepository.save(existingUser);
//        } else {
//            throw new ResourceNotFoundException("User not found with id: " + userId);
//        }
//    }

    @Override
    public DBUser updateUser(Integer userId, DBUser updatedUser) {
        Optional<DBUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            DBUser existingUser = optionalUser.get();

            // Apply partial updates only for the fields that are provided in updatedUser
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }
            if (updatedUser.getToken() != null) {
                existingUser.setToken(updatedUser.getToken());
            }

            System.out.println(existingUser);

            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

//    @Override
//    public List<DBUser> getAllUsers() {
//        return userRepository.findAll();
//    }

    @Override
    public Page<DBUser> getAllUsers(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Optional<DBUser> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public DBUser createUser(DBUser newUser) {
        return userRepository.save(newUser);
    }
}
