package com.ajo.asapp.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.User;
import com.ajo.asapp.entities.message.ImageMessage;
import com.ajo.asapp.entities.message.Message;
import com.ajo.asapp.entities.message.TextMessage;
import com.ajo.asapp.entities.message.VideoMessage;

@Repository("messageDao")
public class MessageMySQLDao extends AbstractIdDaoMySQL<Message, Long> implements MessageDao {

  private static final String MTBL = "messages";
  private static final String UTBL = "users";
  private static final String M_ID_COL = "id";
  private static final String M_AUTH_COL = "author";
  private static final String M_RECP_COL = "recipient";
  
  private static final String U_ID_COL = "id";
  
  public static final String MESSAGES_AUTHOR_SQL =
      "SELECT m.*, u.username, i.*, v.*"
      + " FROM " + MTBL + " m"
      + " INNER JOIN " + UTBL + " u ON m." + M_AUTH_COL + " = u." + U_ID_COL
      + " LEFT OUTER JOIN image_data i on m." + M_ID_COL + " = i.msgid"
      + " LEFT OUTER JOIN video_data v on m." + M_ID_COL + " = v.msgid";
  
  public static final String COND_MID = "m." + M_ID_COL + " = ?";
  public static final String COND_UID = "m." + M_AUTH_COL + " = ?";
  public static final String COND_RECP = "m." + M_RECP_COL + " = ?";
  public static final String COND_RECP_NULL = "m." + M_RECP_COL + " IS NULL";
  
  public static final String ORDER_BY = " ORDER BY m.posted";
  
  public static final String LIMIT = " LIMIT ? OFFSET ?";
  
  public static final String COUNT_SENDER_RECP =
      "SELECT COUNT(m." + M_ID_COL + ") AS c"
      + " FROM " + MTBL + " m"
      + " WHERE ("
      + COND_UID + " AND " + COND_RECP 
      + ") OR ("
      + COND_UID + " AND " + COND_RECP 
      + ")" + ORDER_BY;
  
  public static final String LAST_SEEN_UPSERT =
      "INSERT INTO last_seen VALUES (?,?,?)"
      + " ON DUPLICATE KEY UPDATE tstamp = VALUES(tstamp)";
  
  public static final String LAST_SEEN_CHECK =
      "SELECT 1 as c FROM last_seen WHERE userid = ? AND channel = ?";
  
  public static final String LAST_SEEN_SQL = MESSAGES_AUTHOR_SQL
      + " INNER JOIN last_seen ls"
      + " ON (m.author = ls.channel AND m.recipient = ls.userid)"
      + " OR (m.recipient IS NULL and ls.channel = 0)"
      + " WHERE ls.userid = ? AND m.posted > ls.tstamp"
      + ORDER_BY;
      
  private String msg_sender_recipient_sql;
  
  private SimpleJdbcInsert imageDataAdder;
  private SimpleJdbcInsert videoDataAdder;
  
  @Autowired
  private UserDao userDao;
  
  public MessageMySQLDao() {
    super(MTBL, M_ID_COL);
    this.all_obj_sql = MESSAGES_AUTHOR_SQL + " WHERE " + COND_RECP_NULL + ORDER_BY;
    this.obj_for_id_sql = MESSAGES_AUTHOR_SQL + " WHERE " + COND_MID + ORDER_BY;
    this.msg_sender_recipient_sql = MESSAGES_AUTHOR_SQL
        + " WHERE ("
        + COND_UID + " AND " + COND_RECP 
        + ") OR ("
        + COND_UID + " AND " + COND_RECP 
        + ")" + ORDER_BY;
    
    this.count_sql += " WHERE recipient IS NULL";
  }
  
  @Autowired
  public void setDataSource(DriverManagerDataSource dataSource) {
    super.setDataSource(dataSource);
    this.adder.usingColumns("author", "text", "recipient", "posted");
    
    this.imageDataAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("image_data")
      .usingColumns("msgid", "width", "height");
    
    this.videoDataAdder = new SimpleJdbcInsert(dataSource)
      .withTableName("video_data")
      .usingColumns("msgid", "len", "source", "html");
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
    
    args.clear();
    SimpleJdbcInsert adder = null;
    if(o instanceof ImageMessage) {
      ImageMessage im = (ImageMessage)o;
      args.put("width", im.getWidth());
      args.put("height", im.getHeight());
      
      adder = this.imageDataAdder;
    }
    else if(o instanceof VideoMessage) {
      VideoMessage vm = (VideoMessage)o;
      args.put("len", vm.getLength());
      args.put("source", vm.getSource());
      args.put("html", vm.getHtml());
      
      adder = this.videoDataAdder;
    }
    
    if(adder != null) {
      args.put("msgid", o.getId());
      adder.execute(args);
    }
    
  }

  @Override
  public Collection<Message> getAll() {
    return super.getAll(new MessageRowMapper());
  }
  
  @Override
  public Collection<Message> getDirectMessages(User from, User recipient) {
    return this.jdbcTemplate.query(this.msg_sender_recipient_sql, 
        new Object[] {from.getId(), recipient.getId(), recipient.getId(), from.getId()}, 
        new MessageRowMapper());
  }

  @Override
  public Collection<Message> getDirectMessages(User from, User recipient, int count, int page) {
    int offset = page * count;
    return this.jdbcTemplate.query(this.msg_sender_recipient_sql + LIMIT, 
        new Object[] {from.getId(), recipient.getId(), recipient.getId(), from.getId(), count, offset}, 
        new MessageRowMapper());
  }

  @Override
  public Collection<Message> getAll(int count, int page) {
    int offset = page * count;
    return this.jdbcTemplate.query(this.all_obj_sql + LIMIT, 
        new Object[]{count, offset}, 
        new MessageRowMapper());
  }

  @Override
  public int getCount(User from, User recipient) {
    List<Integer> count = this.jdbcTemplate.query(COUNT_SENDER_RECP, 
        new Object[] {from.getId(), recipient.getId(), recipient.getId(), from.getId()},
        new CountMapper());
    
    return count.get(0);
  }
  
  public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
      
      Message m;
      if(rs.getInt("i.msgid") != 0) {
        ImageMessage im = new ImageMessage();
        im.setWidth(rs.getInt("width"));
        im.setHeight(rs.getInt("height"));
        m = im;
      }
      else if(rs.getInt("v.msgid") != 0) {
        VideoMessage vm = new VideoMessage();
        vm.setHtml(rs.getString("v.html"));
        vm.setSource(rs.getString("v.source"));
        vm.setLength(rs.getInt("v.len"));
        
        m = vm;
      }
      else {
        m = new TextMessage();
      }
      
      m.setId(rs.getLong("m." + M_ID_COL));
      m.setPosted(rs.getLong("m.posted"));
      m.setText(rs.getString("m.text"));
      m.setAuthorId(rs.getInt("m." + M_AUTH_COL));
      m.setAuthorName(rs.getString("u.username"));
      
      int recip = rs.getInt("m.recipient");
      if(recip != 0) {
        m.setRecipient(recip);
      }
      
      return m;
    }
    
  }

  @Override
  public Collection<Message> getNewMessages(User u) {
    return this.jdbcTemplate.query(LAST_SEEN_SQL, new Object[]{u.getId()}, new MessageRowMapper());
  }

  @Override
  public void setLastSeen(User u, User channel, int timestamp) {
    Object channelId;
    if(channel == null) {
      channelId = Types.NULL;
    }
    else {
      channelId = channel.getId();
      List<Integer> l = this.jdbcTemplate.query(LAST_SEEN_CHECK, 
          new Object[]{channelId, u.getId()},
          new CountMapper());
      
      if(l.isEmpty()) {
        this.jdbcTemplate.update(LAST_SEEN_UPSERT, channelId, u.getId(), 0);
      }
    }
    
    this.jdbcTemplate.update(LAST_SEEN_UPSERT, u.getId(), channelId, timestamp);
    
  }
  
}
