package com.contractormanagement.backend.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.contractormanagement.backend.entity.User;

import com.contractormanagement.backend.repository.UserRepository;

import io.jsonwebtoken.lang.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService{
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    try {
      User targetUser = userRepository.findByEmail(email);
      return new org.springframework.security.core.userdetails.User(targetUser.getEmail(),targetUser.getPassword_hash(),Collections.emptyList());
    } catch (Exception e) {
      throw new UsernameNotFoundException("unable to find user with email: " + email);
    }
    
    
  };
}
