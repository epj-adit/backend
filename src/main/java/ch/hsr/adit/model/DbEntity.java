package ch.hsr.adit.model;

import java.io.Serializable;

public interface DbEntity extends Serializable {
  public int getId();

  public void setId(int id);
}
