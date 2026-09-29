package com.contractormanagement.backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.contractormanagement.backend.dto.UserResponse;
import com.contractormanagement.backend.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public User findByEmail(String email);
  public boolean existsByEmail(String email);

  @Query("SELECT new com.contractormanagement.backend.dto.UserResponse(u.id,u.name,u.email,u.role) FROM User u")
  List<UserResponse> findAllUsers();

}
