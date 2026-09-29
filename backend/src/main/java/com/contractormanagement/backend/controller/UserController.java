package com.contractormanagement.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.contractormanagement.backend.dto.UserLoginRequest;
import com.contractormanagement.backend.dto.UserResponse;
import com.contractormanagement.backend.dto.UserUpdateRequest;
import com.contractormanagement.backend.entity.User;
import com.contractormanagement.backend.mapper.UserMapper;
import com.contractormanagement.backend.repository.UserRepository;
import com.contractormanagement.backend.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Autowired 
  AuthenticationManager authenticationManager;

  @Autowired 
  PasswordEncoder encoder;

  @Autowired
  JwtService jwtService;


  public UserController(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  // get all users
  @ResponseBody
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    try {
      userRepository.findAllUsers();
      return ResponseEntity.ok(userRepository.findAllUsers()); 
    } catch (Exception e) {
      System.out.println(e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

  }
  // get user by id
  @ResponseBody
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  public User getUserById(@PathVariable long id) {

    User targetUser = userRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));
    return targetUser;
  }

  // create new user
  @PostMapping("/create")
  @PreAuthorize ("hasRole('ADMIN')")
  public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequest newUserRequest) {
    try {
        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
          return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered!");
        }
      User newUser = userMapper.createEntity(newUserRequest);
      userRepository.save(newUser);
      return ResponseEntity.ok("user created successfully! " + newUser);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to create new user! Error:" + e);
    }

  }

  // update user
  @PatchMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  public ResponseEntity<String> updateUser(@PathVariable long id,@Valid @RequestBody UserUpdateRequest updates) {

    try {
      User targetUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));

      User updatedUser = userMapper.updateEntity(updates, targetUser);
      userRepository.save(updatedUser);
      return ResponseEntity.ok("user updated successfully! " + updatedUser);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to update user! " + e);
    }
  }

  // delete user by id
  @DeleteMapping("/delete/{id}")
  @PreAuthorize ("hasRole('ADMIN')")
  public ResponseEntity<String> deleteUserById(@PathVariable long id) {
    try {
      User targetUser = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find user with id:" + id));

      userRepository.delete(targetUser);
      return ResponseEntity.ok("user id " + id + " is deleted successfully! \n");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"fail to delete user! " + e);
    }
  }

  @PostMapping("/login")
  public String login(@RequestBody UserLoginRequest user) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
      );
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return jwtService.generateJwt(userDetails.getUsername());
      // return ResponseEntity.ok("user login successfully");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"login failed! "+ e);
    }
  }

}

  

