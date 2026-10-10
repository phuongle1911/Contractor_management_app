package com.contractormanagement.backend.mapper;

import com.contractormanagement.backend.dto.ProjectUpdateRequest;
import com.contractormanagement.backend.entity.Project;

public class ProjectMapper {
  public Project updateProject(ProjectUpdateRequest request, Project projectToUpdate) {
    if (request.getName() != null) {
      projectToUpdate.setName(request.getName());
    }
    if (request.getContractor_budget() > 0) {
      projectToUpdate.setContractor_budget(request.getContractor_budget());
    }
    if (request.getLocation() != null) {
      projectToUpdate.setLocation(request.getLocation());
    }
    if (request.getScope_of_works() != null) {
      projectToUpdate.setScope_of_works(request.getScope_of_works());
    }

    return projectToUpdate;
  }
}
