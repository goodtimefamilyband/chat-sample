package com.ajo.asapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.ajo.asapp.repos.UserDao;
import com.ajo.asapp.repos.UserMySQLDao;

@Configuration
public class AuthConfig {

  @Autowired
  private DriverManagerDataSource dataSource;
  
  
  @Autowired
  private UserDao userDao;
  
  private static final String USERS_BY_USERNAME_SQL = 
      "SELECT username, password, enabled FROM users WHERE username = ?";
  
  private static final String AUTHORITIES_BY_USERNAME_SQL = 
      "SELECT u.username, r.rolename"
      + " FROM users u"
      + " INNER JOIN role_user ru ON u.id = ru.userid"
      + " INNER JOIN roles r ON ru.roleid = r.id"
      + " WHERE u.username = ?";
  
  
  @Bean(name="userDetailsService")
  public UserDetailsService userDetailsService(){
    return userDao;
  }
  
}
