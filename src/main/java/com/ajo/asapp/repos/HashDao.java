package com.ajo.asapp.repos;

import java.util.HashMap;

import com.ajo.asapp.entities.AbstractIdItem;

public abstract class HashDao<Obj extends AbstractIdItem<IdType>, IdType> implements AbstractIdDao<Obj, IdType> {

  protected HashMap<IdType, Obj> map = new HashMap<>();
  
  protected abstract IdType getNextId();
  
  public Obj getForId(IdType id) {
    return map.get(id);
  }
  
  public void add(Obj o) {
    o.setId(getNextId());
    map.put(o.getId(), o);
  }
  
}
