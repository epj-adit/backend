package ch.hsr.adit.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "role", schema = "public",
    uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private String name;
  private Set<Permission> permissions = new HashSet<Permission>(0);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
  @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq",
      initialValue = 10, allocationSize = 1)
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

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "role_permission", schema = "public",
      joinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)},
      inverseJoinColumns = {
          @JoinColumn(name = "permission_id", nullable = false, updatable = false)})
  public Set<Permission> getPermissions() {
    return this.permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }



}


