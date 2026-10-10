package com.contractormanagement.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter 
@NoArgsConstructor 
public class ProjectUpdateRequest {

  @Column(name = "name")
  @Size(min= 2)
  private String name;

  @Column(name="contractor_budget")
  private int contractor_budget;

  @Column (name="location")
  private String location;

  @Column (name = "scope_of_works")
  private String scope_of_works;
}
