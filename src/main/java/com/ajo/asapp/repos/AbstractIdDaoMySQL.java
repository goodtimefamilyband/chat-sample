package com.ajo.asapp.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.ajo.asapp.entities.AbstractIdItem;

@Repository
public abstract class AbstractIdDaoMySQL<ObjType extends AbstractIdItem<IdType>, IdType> implements AbstractIdDao<ObjType, IdType> {

  public static final String OBJ_FOR_ID_SQL = 
      "SELECT * FROM :tbl WHERE :id_col = ?";
  
  public static final String COUNT_SQL =
      "SELECT COUNT(:id_col) AS c FROM :tbl";
  
  public static final String ALL_OBJ_SQL = "SELECT * FROM :tbl";
  
  protected String obj_for_id_sql;
  protected String all_obj_sql;
  protected String count_sql;
  
  protected JdbcTemplate jdbcTemplate;
  
  protected SimpleJdbcInsert adder;
  protected String tbl;
  protected String id_col;
  
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    
    this.adder = new SimpleJdbcInsert(dataSource)
    .withTableName(this.tbl)
    .usingGeneratedKeyColumns(this.id_col);
  }
  
  protected void buildQueryBases(String tbl, String id_col) {
    
    obj_for_id_sql = OBJ_FOR_ID_SQL
        .replace(":tbl", tbl)
        .replace(":id_col", id_col);
    
    all_obj_sql = ALL_OBJ_SQL.replace(":tbl", tbl);
    
    count_sql = COUNT_SQL
        .replace(":tbl", tbl)
        .replace(":id_col", id_col);
  }
  
  protected AbstractIdDaoMySQL(String tbl, String id_col) {
    this.tbl = tbl;
    this.id_col = id_col;
    
    buildQueryBases(tbl, id_col);
  }
  
  public ObjType getForId(IdType id, RowMapper<ObjType> mapper) {
    List<ObjType> objList = this.jdbcTemplate.query(obj_for_id_sql, new Object[]{id}, mapper);
    if(objList.size() == 0) {
      return null;
    }
    
    return objList.get(0);
  }
  
  public List<ObjType> getAll(RowMapper<ObjType> rm) {
    return this.jdbcTemplate.query(this.all_obj_sql, rm);
  }
  
  public int getCount() {
    List<Integer> count = this.jdbcTemplate.query(this.count_sql, new CountMapper());
    
    return count.get(0);
  }
  
  public class CountMapper implements RowMapper<Integer> {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getInt("c");
    }
  }
  
}
