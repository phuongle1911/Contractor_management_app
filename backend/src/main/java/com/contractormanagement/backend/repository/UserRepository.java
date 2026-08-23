package com.contractormanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contractormanagement.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);
  
}
