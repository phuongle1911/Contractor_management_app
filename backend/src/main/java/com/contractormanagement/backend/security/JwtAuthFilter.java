package com.contractormanagement.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Autowired 
  private JwtService jwtService;

  @Autowired 
  private CustomUserDetailsService userDetailsService;

  private String parseJwt(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  @Override 
  protected void doFilterInternal(
    HttpServletRequest request, 
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException{
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtService.validateJwt(jwt)) {
        String userEmail = jwtService.getEmailFromJwt(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch(Exception e) {
      System.err.println("fail to set user authentication: " + e);
    }
    filterChain.doFilter(request,response);
  }
}
