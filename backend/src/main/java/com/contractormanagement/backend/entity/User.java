package com.contractormanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
public class User {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  private String email;
  private String name;
  private String password;
  private String role;
  private String status;

  @AllArgsConstructor
  @Getter
  @Setter


}
