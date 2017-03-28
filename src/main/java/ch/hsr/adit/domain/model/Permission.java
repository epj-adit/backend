package ch.hsr.adit.domain.model;
// Generated 23.03.2017 11:05:29 by Hibernate Tools 5.2.1.Final


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Permission generated by hbm2java
 */
@Entity
@Table(name = "permission", schema = "public",
    uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Permission implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private String name;

  public Permission() {}


  public Permission(long id, String name) {
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


