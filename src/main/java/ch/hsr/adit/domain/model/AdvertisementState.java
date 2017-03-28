package ch.hsr.adit.domain.model;
// Generated 23.03.2017 11:05:29 by Hibernate Tools 5.2.1.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * AdvertisementState generated by hbm2java
 */
@Entity
@Table(name = "advertisement_state", schema = "public",
    uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class AdvertisementState implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private String name;

  public AdvertisementState() {}


  public AdvertisementState(long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Id
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }


  @Column(name = "name", unique = true, nullable = false)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

}


