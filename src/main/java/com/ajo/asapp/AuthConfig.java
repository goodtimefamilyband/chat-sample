package com.ajo.asapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@Configuration
public class AuthConfig {

  @Autowired
  private DriverManagerDataSource dataSource;

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
    System.out.println("Datasource: " + this.dataSource.getUrl());
    JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
    jdbcImpl.setDataSource(this.dataSource);
    jdbcImpl.setUsersByUsernameQuery(USERS_BY_USERNAME_SQL);
    jdbcImpl.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_SQL);
    return jdbcImpl;
  }
  
}
