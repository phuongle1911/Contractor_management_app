package com.contractormanagement.backend.entity;

import jakarta.persistence.*;

@Entity
public class User {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String email;
  private String name;
  private String password;
  private String role;
  private String status;

  public User(String )

}
