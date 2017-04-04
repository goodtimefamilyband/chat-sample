package com.ajo.asapp.repos;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractIdDAO<ObjType> {

  public static final String OBJ_FOR_ID_SQL = 
      "SELECT * FROM :tbl WHERE :id_col = ?";
  
  public static final String ALL_OBJ_SQL = "SELECT * FROM :tbl";
  
  protected String obj_for_id_sql;
  protected String all_obj_sql;
  
  protected JdbcTemplate jdbcTemplate;
  
  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }
  
  protected final void buildQueryBases(String tbl, String id_col) {
    
    obj_for_id_sql = OBJ_FOR_ID_SQL
        .replace(":tbl", tbl)
        .replace(":id_col", id_col);
    
    all_obj_sql = ALL_OBJ_SQL.replace(":tbl", tbl);
    
  }
  
  protected AbstractIdDAO(String tbl, String id_col) {
    buildQueryBases(tbl, id_col);
  }
  
  public ObjType getForId(long id, RowMapper<ObjType> mapper) {
    List<ObjType> objList = this.jdbcTemplate.query(obj_for_id_sql, new Object[]{id}, mapper);
    if(objList.size() == 0) {
      return null;
    }
    
    return objList.get(0);
  }
  
  public List<ObjType> getAll(RowMapper<ObjType> rm) {
    return this.jdbcTemplate.query(this.all_obj_sql, rm);
  }
}
