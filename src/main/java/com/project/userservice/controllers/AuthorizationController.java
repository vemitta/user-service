package com.project.userservice.controllers;

import com.project.userservice.mappers.UserMapper;
import com.project.userservice.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthorizationController {

    @Autowired
    private UserMapper userMapper;
//    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Requestor-Type", exposedHeaders = "X-Get-Header")
    @GetMapping("/authorization/permissions")
//    @RequestHeader("X-USER-TOKEN") String userToken
    public List<PermissionName> getPermissions() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        switch (user.getRole()) {
            case ADMIN:
                return List.of(PermissionName.CREATE_PROJECT, PermissionName.MANAGE_USERS, PermissionName.VIEW_USER_ACTIVITY, PermissionName.CREATE_OPENING, PermissionName.VIEW_PENDING_APPLICATIONS, PermissionName.VIEW_REPORTS, PermissionName.MANAGE_OPENINGS, PermissionName.REGISTER_USER, PermissionName.ADMIN_OWN_OPENINGS, PermissionName.ADMIN_OTHER_OPENINGS);
            case RECRUITER:
                return List.of(PermissionName.CREATE_OPENING, PermissionName.VIEW_PENDING_APPLICATIONS, PermissionName.MANAGE_OPENINGS, PermissionName.VIEW_REPORTS, PermissionName.REGISTER_USER, PermissionName.RECRUITER_OWN_OPENINGS, PermissionName.RECRUITER_OTHER_OPENINGS);
            case EMPLOYEE:
                return List.of(PermissionName.VIEW_ALL_OPENINGS, PermissionName.REGISTER_USER, PermissionName.VIEW_APPLIED_OPENINGS);
        }
        return null;
    }

    @GetMapping("/authorization/role")
    public Role getRole() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole();
    }

    @GetMapping("/authorization/user")
    public PublicUser getUser() {
        DBUser user = (DBUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.entityToPublicModel(user);
    }
}
