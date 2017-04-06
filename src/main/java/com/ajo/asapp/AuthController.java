package com.ajo.asapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.repos.RoleDao;
import com.ajo.asapp.repos.UserDao;

@Controller
public class AuthController {

  @Autowired
  private UserDao userDao;
  
  @Autowired
  private RoleDao roleDao;
  
  @GetMapping("/create")
  public String create() {
    return "create";
  }
  
  @PostMapping("/create") 
  public String signup(
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "password2") String password2,
      HttpServletRequest req,
      ModelMap model
      ){
    
    if(!password.equals(password2)) {
      model.addAttribute("error", "Passwords do not match");
      return "create";
    }
    
    User u = userDao.getForName(username);
    if(u != null) {
      model.addAttribute("error", "There is already a user by that name");
      return "create";
    }
    
    u = new User();
    u.setName(username);
    userDao.add(u);
    userDao.setPassword(u, password);
    
    roleDao.addRoleToUser(u, roleDao.getDefaultRole());
    
    model.addAttribute("msg", "User was added successfully with ID " + u.getId());
    return "create";
  }
  
  @GetMapping("/logout")
  public String logout() {
    return "logout";
  }
  
}
