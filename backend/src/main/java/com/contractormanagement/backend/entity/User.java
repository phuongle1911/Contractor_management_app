package com.contractormanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, length=256)
  private String email;

  @Column(name = "name", nullable = false, length=256)
  private String name;

  @Column(name = "password", nullable = false, length=256)
  private String password;

  @Column(name="role")
  private String role;

  @Column(name="status", nullable=false)
  private String status;

}
