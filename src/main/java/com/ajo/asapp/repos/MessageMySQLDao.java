package com.ajo.asapp.repos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.Message;
import com.ajo.asapp.entities.User;

@Repository("messageDao")
public class MessageMySQLDao extends AbstractIdDaoMySQL<Message, Long> implements MessageDao {

  private static final String MTBL = "messages";
  private static final String UTBL = "users";
  private static final String M_ID_COL = "id";
  private static final String M_AUTH_COL = "author";
  private static final String M_RECP_COL = "recipient";
  
  private static final String U_ID_COL = "id";
  
  public static final String MESSAGES_AUTHOR_SQL =
      "SELECT m.*, u.username"
      + " FROM " + MTBL + " m"
      + " INNER JOIN " + UTBL + " u ON m." + M_AUTH_COL + " = u." + U_ID_COL;
  
  public static final String COND_MID = "m." + M_ID_COL + " = ?";
  public static final String COND_UID = "m." + M_AUTH_COL + " = ?";
  public static final String COND_RECP = "m." + M_RECP_COL + " = ?";
  public static final String COND_RECP_NULL = "m." + M_RECP_COL + " IS NULL";
  
  private String msg_sender_recipient_sql;
  
  @Autowired
  private UserDao userDao;
  
  public MessageMySQLDao() {
    super(MTBL, M_ID_COL);
    this.all_obj_sql = MESSAGES_AUTHOR_SQL + " WHERE " + COND_RECP_NULL;
    this.obj_for_id_sql = MESSAGES_AUTHOR_SQL + " WHERE " + COND_MID;
    this.msg_sender_recipient_sql = MESSAGES_AUTHOR_SQL
        + " WHERE ("
        + COND_UID + " AND " + COND_RECP 
        + ") OR ("
        + COND_UID + " AND " + COND_RECP 
        + ")";
  }
  
  @Autowired
  public void setDataSource(DriverManagerDataSource dataSource) {
    super.setDataSource(dataSource);
    this.adder.usingColumns("author", "text", "recipient", "posted");
  }

  @Override
  public Message getForId(Long id) {
    return super.getForId(id, new MessageRowMapper());
  }

  @Override
  public void add(Message o) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("author", o.getAuthorId());
    args.put("text", o.getText());
    args.put("recipient", o.getRecipient());
    args.put("posted", o.getPosted());
    
    Number id = this.adder.executeAndReturnKey(args);
    o.setId(id.longValue());
    
  }

  @Override
  public Collection<Message> getAll() {
    // TODO Auto-generated method stub
    return super.getAll(new MessageRowMapper());
  }

  public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      
      Message m = new Message();
      m.setId(rs.getLong("m." + M_ID_COL));
      m.setPosted(rs.getLong("m.posted"));
      m.setText(rs.getString("m.text"));
      m.setAuthorId(rs.getInt("m." + M_AUTH_COL));
      m.setAuthorName(rs.getString("u.username"));
      
      return m;
    }
    
  }

  @Override
  public Collection<Message> getDirectMessages(User from, User recipient) {
    return this.jdbcTemplate.query(this.msg_sender_recipient_sql, 
        new Object[] {from.getId(), recipient.getId(), recipient.getId(), from.getId()}, 
        new MessageRowMapper());
  }
  
}
