package com.ajo.asapp.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.User;

@Repository("userDao")
public class UserMySQLDao extends AbstractIdDaoMySQL<User, Integer> implements UserDao, UserDetailsService {

  private static final String USERID_FOR_USERNAME_SQL =
      "SELECT id FROM users WHERE username = ?";
  
  private static final String USER_FOR_USERNAME_SQL =
      "SELECT * FROM users WHERE username = ? AND enabled = TRUE";
  
  private static final String SET_PASSWORD_SQL =
      "UPDATE users SET password = ? WHERE id = ?";
  
  @Lazy
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private RoleDao roleDao;
  
  @Autowired
  public void setDataSource(DriverManagerDataSource dataSource) {
    super.setDataSource(dataSource);
    
    this.adder.usingColumns("username");
  }
  
  public UserMySQLDao() {
    super("users", "id");
    this.obj_for_id_sql += " AND enabled = TRUE";
  }

  @Override
  public void add(User o) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("username", o.getName());
    Number id = this.adder.executeAndReturnKey(args);
    o.setId(id.intValue());
  }

  @Override
  public User getForName(String name) {
    List<User> uList = this.jdbcTemplate.query(USER_FOR_USERNAME_SQL, new Object[]{name}, new UserRowMapper());
    if(uList.isEmpty()) {
      return null;
    }
    
    return uList.get(0);
  }

  @Override
  public User getForId(Integer id) {
    return super.getForId(id, new UserRowMapper());
  }
  
  public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User u = new User();
      u.setId(rs.getInt("id"));
      u.setName(rs.getString("username"));
      u.setEnabled(rs.getBoolean("enabled"));
      u.setPassword(rs.getString("password"));
      u.setAuthorities(UserMySQLDao.this.roleDao.getRolesForUser(u));
      return u;
    }
    
  }

  @Override
  public void setPassword(User u, String password) {
    String cpass = passwordEncoder.encode(password);
    
    this.jdbcTemplate.update(SET_PASSWORD_SQL, cpass, u.getId());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.getForName(username);
  }

}
