package com.ajo.asapp.repos;

import java.util.HashMap;

import com.ajo.asapp.entities.AbstractIdItem;

public class HashDao<Obj extends AbstractIdItem<IdType>, IdType> implements AbstractIdDao<Obj, IdType> {

  protected HashMap<IdType, Obj> map = new HashMap<>();
  
  public Obj getForId(IdType id) {
    return map.get(id);
  }
  
  public void add(Obj o) {
    map.put(o.getId(), o);
  }
  
}
