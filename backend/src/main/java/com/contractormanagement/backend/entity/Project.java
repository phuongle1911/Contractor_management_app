package com.contractormanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "projects")
@Getter
@Setter

public class Project {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  @Setter(AccessLevel.NONE)
  private Long id;

  @NotBlank 
  @Column(name = "name")
  @Size(min= 2)
  private String name;

  @NotNull 
  @Column(name="contractor_budget")
  private int contractor_budget;

  @NotNull 
  @Column (name="location")
  private String location;

  @NotBlank 
  @Column (name = "scope_of_works")
  private String scope_of_works;


}
