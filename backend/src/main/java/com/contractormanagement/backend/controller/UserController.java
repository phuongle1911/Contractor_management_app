package com.contractormanagement.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.contractormanagement.backend.dto.UserCreateRequest;
import com.contractormanagement.backend.dto.UserUpdateRequest;
import com.contractormanagement.backend.entity.User;
import com.contractormanagement.backend.mapper.UserMapper;
import com.contractormanagement.backend.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserController(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  // get all users
  @ResponseBody
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    try {
      userRepository.findAll();
      return ResponseEntity.ok(userRepository.findAll()); 
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to get all users!");
    }

  }
  // get user by id
  @ResponseBody
  @GetMapping("/{id}")
  public User getUserById(@PathVariable long id) {

    User targetUser = userRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));
    return targetUser;
  }

  // create new user
  @PostMapping("/create")
  public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequest newUserRequest) {
    try {
      User newUser = userMapper.createEntity(newUserRequest);
      userRepository.save(newUser);
      return ResponseEntity.ok("user created successfully! " + newUser);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to save new user! Error:" + e);
    }

  }

  // update user
  @PatchMapping("/update/{id}")
  public ResponseEntity<String> updateUser(@PathVariable long id,@Valid @RequestBody UserUpdateRequest updates) {

    try {
      User targetUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));

      User updatedUser = userMapper.updateEntity(updates, targetUser);
      userRepository.save(updatedUser);
      return ResponseEntity.ok("user updated successfully! " + updatedUser);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to get update user with id " + id);
    }
  }

  // delete user by id
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable long id) {
    try {
      User targetUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));

      userRepository.delete(targetUser);
      return ResponseEntity.ok("user id " + id + " is deleted successfully! \\n");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to delete user with id " + id);
    }
  }

  // @PostMapping("/login")
  // public ResponseEntity<String> login() {

  // }
}

  

