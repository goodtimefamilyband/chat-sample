package com.ajo.asapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
  
  public static final String LOGIN_PATH = "/login";
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/app/**").authenticated()
      .and().formLogin()
      .and().logout()
      .permitAll()
      .and().csrf().csrfTokenRepository(csrfTokenRepository());
  }
  
  private CsrfTokenRepository csrfTokenRepository() 
  { 
      HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
      repository.setSessionAttributeName("_csrf");
      return repository; 
  }
  
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }

}
