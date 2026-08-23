package com.contractormanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Getter
  @Setter
  private Long id;
  private String email;
  private String name;
  private String password;
  private String role;
  private String status;




}
