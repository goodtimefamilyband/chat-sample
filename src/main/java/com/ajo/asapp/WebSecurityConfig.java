package com.ajo.asapp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
  public static final String LOGIN_PATH = "/login";
  
  @Autowired
  DataSource dataSource;
  
  @Autowired
  UserDetailsService userDetailsService;
  
  @Bean
  public DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.authenticationProvider(authProvider())
          .authorizeRequests()
              .antMatchers("/app/**").authenticated()
              .anyRequest().permitAll()
              .and()
          .formLogin()
              //.loginPage(LOGIN_PATH)
              .usernameParameter("username").passwordParameter("password")
              .permitAll()
              .and()
          .logout().logoutUrl("/logout").invalidateHttpSession(true)
              .permitAll()
          .and().csrf()
            .csrfTokenRepository(csrfTokenRepository());
  }
  
  private CsrfTokenRepository csrfTokenRepository() 
  { 
      HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
      repository.setSessionAttributeName("_csrf");
      return repository; 
  }
  
  @Bean(name="passwordEncoder")
  public PasswordEncoder passwordEncoder(){
   return new BCryptPasswordEncoder();
  }
  
}
