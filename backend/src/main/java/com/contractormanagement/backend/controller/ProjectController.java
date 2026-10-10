package com.contractormanagement.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.contractormanagement.backend.dto.ProjectUpdateRequest;
import com.contractormanagement.backend.entity.Project;
import com.contractormanagement.backend.mapper.ProjectMapper;
import com.contractormanagement.backend.repository.ProjectRepository;

import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectRepository projectRepository;
  private  final ProjectMapper projectMapper;

  public ProjectController(ProjectRepository projectRepository, ProjectMapper projectMapper) {
    this.projectRepository = projectRepository;
    this.projectMapper = projectMapper;
  }

  // get all projects
  @ResponseBody 
  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<Project>> getAllProjects() {
    try {
      return ResponseEntity.ok(projectRepository.findAll());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  // get project by id
  @ResponseBody 
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public Project getProjectById(@PathVariable long id) {
    Project targetProject = projectRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find project with id: " + id));
    return targetProject;
  }

  // create new project
  @ResponseBody 
  @GetMapping("/create")
  @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
  public ResponseEntity<String> createProject(@Valid @RequestBody Project newProject) {
    try {
      projectRepository.save(newProject);
      return ResponseEntity.ok("project created successfully!");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to create new project!");
    }
  }

  // update project
  @ResponseBody 
  @PatchMapping("/update/{id}")
  @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
  public ResponseEntity<String> updateProject(@PathVariable long id, @Valid @RequestBody ProjectUpdateRequest updates) {
    try {
      Project targetProject = projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find project with id:" + id));
      
    Project updatedProject =  projectMapper.updateProject(updates, targetProject);
    projectRepository.save(updatedProject);
    return ResponseEntity.ok("project updated successfully! " + updatedProject);
   
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to update project! ");
    }
  }

  @ResponseBody
  @DeleteMapping("/delete/{id}")
  @PreAuthorize ("hasRole('MANAGER') or hasRole('ADMIN')")
  public ResponseEntity<String> deleteProjectById(@PathVariable long id) {
    try {
      Project targetProject = projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find project with id:" + id));
      projectRepository.delete(targetProject);
      return ResponseEntity.ok("project id " + id + " is deleted successfully!");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to delete project! ");
    }
  }



}
