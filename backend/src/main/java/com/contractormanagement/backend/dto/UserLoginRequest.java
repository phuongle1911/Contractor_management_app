package com.contractormanagement.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest {
  @Size(max = 255) @Email 
  private String email;

  private String password;

}
