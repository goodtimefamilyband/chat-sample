package com.ajo.asapp.repos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.Message;

@Repository("messageDao")
public class MessageMySQLDao extends AbstractIdDaoMySQL<Message, Long> implements MessageDao {

  public MessageMySQLDao() {
    super("messages", "id");
  }
  
  @Autowired
  public void setDataSource(DriverManagerDataSource dataSource) {
    super.setDataSource(dataSource);
  
    this.adder.usingColumns("author", "text", "recipient");
    this.adder.usingGeneratedKeyColumns("id", "posted");
  }

  @Override
  public Message getForId(Long id) {
    return super.getForId(id, new MessageRowMapper());
  }

  @Override
  public void add(Message o) {
    HashMap<String, Object> args = new HashMap<>();
    args.put("author", o.getUserId());
    args.put("text", o.getText());
    args.put("recipient", o.getRecipient());
    
    Map<String, Object> keys = this.adder.executeAndReturnKeyHolder(args).getKeys();
    o.setId((Long)keys.get("id"));
    o.setPosted((Date)keys.get("posted"));
  }

  @Override
  public Collection<Message> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      Message m = new Message();
      m.setId(rs.getLong("id"));
      m.setPosted(rs.getDate("posted"));
      m.setText(rs.getString("text"));
      m.setUserId(rs.getInt("userid"));
      return m;
    }
    
  }
  
}
