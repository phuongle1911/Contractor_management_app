package com.contractormanagement.backend.dto;

import com.contractormanagement.backend.entity.UserRole;
import com.contractormanagement.backend.entity.UserStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

  @Size(min= 3, max = 255)
  private String name;

  @Size(max = 255) @Email 
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  public void setName(String name) {
    this.name = name.trim();
  }

}
