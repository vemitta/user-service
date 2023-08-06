package com.project.userservice.mappers;

import com.project.userservice.models.DBUser;
import com.project.userservice.models.PublicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Autowired
    private SkillMapper skillMapper;

    public List<PublicUser> entityToPublicModel(List<DBUser> dbUsers) {
        if (dbUsers == null) {
            return new ArrayList<>();
        }
        return dbUsers.stream().map(this::entityToPublicModel).collect(Collectors.toList());
    }
    public PublicUser entityToPublicModel(DBUser dbUser) {
        if (dbUser != null) {
            return new PublicUser(dbUser.getId(), dbUser.getName(), dbUser.getEmail(), dbUser.getRole(), skillMapper.entityToModel(dbUser.getSkills()));
        } else {
            return null;
        }
    }
}
