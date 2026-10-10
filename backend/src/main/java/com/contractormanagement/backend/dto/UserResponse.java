package com.contractormanagement.backend.dto;

import com.contractormanagement.backend.entity.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private UserRole role;
  

}
