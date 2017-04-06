package com.ajo.asapp.repos;

import com.ajo.asapp.entities.Role;
import com.ajo.asapp.entities.User;

public interface RoleDao extends AbstractIdDao<Role, Integer> {
  
  public Role getForName(String name);
  
  public Role getDefaultRole();
  
  public void addRoleToUser(User u, Role r);
  
}
