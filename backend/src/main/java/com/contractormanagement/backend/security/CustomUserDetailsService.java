package com.contractormanagement.backend.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contractormanagement.backend.entity.User;
import com.contractormanagement.backend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User targetUser = userRepository.findByEmail(email);
    if(targetUser == null) {
      throw new UsernameNotFoundException("unable to find user with email: " + email);
    }
    return new UserPrincipal(targetUser);
  };
}
