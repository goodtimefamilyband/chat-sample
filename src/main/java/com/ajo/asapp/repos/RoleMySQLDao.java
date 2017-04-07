package com.ajo.asapp.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.Role;
import com.ajo.asapp.entities.User;

@Repository("roleDao")
public class RoleMySQLDao extends AbstractIdDaoMySQL<Role, Integer> implements RoleDao {

  private static final String COL_ROLENAME = "rolename";
  
  private static final String ROLE_FOR_NAME_SQL =
      "SELECT * FROM roles WHERE " + COL_ROLENAME + " = ?";
  
  private static final String ROLES_FOR_USER_SQL =
      "SELECT * FROM roles r"
      + " INNER JOIN role_user ru ON r.id = ru.roleid"
      + " INNER JOIN users u ON ru.userid = u.id"
      + " WHERE u.id = ?";
  
  protected RoleMySQLDao() {
    super("roles", "id");
  }
  
  private SimpleJdbcInsert roleUserAdder;
  
  private Role defaultRole;
  
  @Autowired
  public void setDataSource(DriverManagerDataSource dataSource) {
    super.setDataSource(dataSource);
    
    this.adder.usingColumns(COL_ROLENAME);
    this.roleUserAdder = new SimpleJdbcInsert(dataSource)
    .withTableName("role_user")
    .usingColumns("userid", "roleid");
    
    this.defaultRole = this.getForName("ROLE_USER");
    
  }

  @Override
  public void add(Role o) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("rolename", o.getName());
    Number id = this.adder.executeAndReturnKey(args);
    o.setId(id.intValue());
  }

  @Override
  public Role getForId(Integer id) {
    return super.getForId(id, new RoleMapper());
  }

  @Override
  public Role getForName(String name) {
    // TODO Auto-generated method stub
    List<Role> rList = this.jdbcTemplate.query(ROLE_FOR_NAME_SQL, new Object[]{name}, new RoleMapper());
    if(rList.isEmpty()) {
      return null;
    }
    
    return rList.get(0);
  }

  @Override
  public Role getDefaultRole() {
    return this.defaultRole;
  }

  @Override
  public void addRoleToUser(User u, Role r) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("userid", u.getId());
    args.put("roleid", r.getId());
    
    roleUserAdder.execute(args);
  }
  
  public class RoleMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
      Role r = new Role();
      r.setId(rs.getInt(RoleMySQLDao.this.id_col));
      r.setName(rs.getString(COL_ROLENAME));
      
      return r;
    }
    
  }

  @Override
  public Collection<Role> getRolesForUser(User u) {
    List<Role> roles = this.jdbcTemplate.query(ROLES_FOR_USER_SQL, new Object[]{u.getId()}, new RoleMapper());
    return roles;
  }

}
