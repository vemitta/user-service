package com.project.userservice.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserListResponse {
    List<PublicUser> users;
    Long totalElements;
}
