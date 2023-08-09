package com.project.userservice.models;

import lombok.Data;

@Data
public class AdminUserRequest {
    private String name;
    private String email;
    private Role role;
}
