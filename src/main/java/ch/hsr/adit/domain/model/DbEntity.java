package ch.hsr.adit.domain.model;

import java.io.Serializable;

public interface DbEntity extends Serializable {
  public long getId();

  public void setId(long id);
}
