package com.contractormanagement.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.contractormanagement.backend.entity.User;
import com.contractormanagement.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @ResponseBody
  @GetMapping
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable long id) {
    try {
      return userRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }


  @PostMapping("/create")
  public ResponseEntity<String> createUser(@RequestBody User newUser) {
    try {
        userRepository.save(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  

  

}
