package com.contractormanagement.backend.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.contractormanagement.backend.dto.UserCreateRequest;
import com.contractormanagement.backend.dto.UserUpdateRequest;
import com.contractormanagement.backend.entity.User;
@Component
public class UserMapper {

  private final PasswordEncoder passwordEncoder;

  public UserMapper(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public User createEntity(UserCreateRequest request) {
    User newUser = new User();

    newUser.setEmail(request.getEmail());
    newUser.setName(request.getName());

    String password_hash = passwordEncoder.encode(request.getPassword());
    newUser.setPassword_hash(password_hash);

    newUser.setRole(request.getRole());
    newUser.setStatus(request.getStatus());

    return newUser;

  }
  public User updateEntity(UserUpdateRequest request, User userToUpdate) {
    if (request.getName() != null) {
      userToUpdate.setName(request.getName());
    }

    if (request.getEmail() != null) {
      userToUpdate.setEmail(request.getEmail());
    }

    if (request.getPassword() != null) {
      String password_hash = passwordEncoder.encode(request.getPassword());
      userToUpdate.setPassword_hash(password_hash);
    }

    if (request.getRole() != null) {
      userToUpdate.setRole(request.getRole());
    }

    if (request.getStatus() != null) {
      userToUpdate.setStatus(request.getStatus());
    }

    return userToUpdate;
  }

}
