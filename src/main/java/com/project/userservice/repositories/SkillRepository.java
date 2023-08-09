package com.project.userservice.repositories;

import com.project.userservice.models.DBSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SkillRepository extends JpaRepository<DBSkill, Long> {
}