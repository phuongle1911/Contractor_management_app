package com.contractormanagement.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.contractormanagement.backend.security.CustomUserDetailsService;
import com.contractormanagement.backend.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired 
  CustomUserDetailsService userDetailsService;

  @Bean
  public JwtAuthFilter jwtFilter() {
    return new JwtAuthFilter();
  }

  @Bean 
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration
  ) {
    return authenticationConfiguration.getAuthenticationManager();
  }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests((authorize) -> authorize
        .requestMatchers("/api/health").permitAll()
        .requestMatchers("/api/users/login").permitAll()
        .requestMatchers("/api/users/**").authenticated()
        .anyRequest().authenticated()
			)
      .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

  // @Bean
  // public AuthenticationProvider authenticationProvider() {
  //   DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
  //   provider.setPasswordEncoder(passwordEncoder());
  //   return provider;
  // };

	@Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }



}
