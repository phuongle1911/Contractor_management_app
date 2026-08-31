package com.contractormanagement.backend.mapper;

import com.contractormanagement.backend.dto.UserUpdateRequest;
import com.contractormanagement.backend.entity.User;

public class UserMapper {
  public User updateEntity(UserUpdateRequest request, User userToUpdate) {
    if (request.getName() != null) {
      userToUpdate.setName(request.getName());
    }

    if (request.getEmail() != null) {
      userToUpdate.setEmail(request.getEmail());
    }

    if (request.getPassword_hash() != null) {
      userToUpdate.setPassword_hash(request.getPassword_hash());
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
