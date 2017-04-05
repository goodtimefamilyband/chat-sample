package com.ajo.asapp.repos;

import com.ajo.asapp.entities.AbstractIdItem;

public interface AbstractIdDao<Obj extends AbstractIdItem<IdType>, IdType> {

  public Obj getForId(IdType id);
  public void add(Obj o);
  
}
