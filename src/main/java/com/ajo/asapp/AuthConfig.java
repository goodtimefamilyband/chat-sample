package com.ajo.asapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ajo.asapp.repos.UserDao;

@Configuration
public class AuthConfig {

  @Autowired
  private DriverManagerDataSource dataSource;
  
  
  @Autowired
  private UserDao userDao;
  
  @Bean(name="userDetailsService")
  public UserDetailsService userDetailsService(){
    return userDao;
  }
  
}
