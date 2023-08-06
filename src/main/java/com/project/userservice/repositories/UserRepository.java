package com.project.userservice.repositories;

import com.project.userservice.models.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Integer> {
   Optional<DBUser> findByEmail(Object email);
}
