package ch.hsr.adit.domain.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "user", schema = "public",
    uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements DbEntity {
  
  private static final long serialVersionUID = 1L;

  @Transient
  private String passwordPlaintext;
  
  private long id;
  private Role role;
  private String username;
  private String email;
  private String passwordHash;
  private String jwtToken;
  private boolean isPrivate;
  private boolean wantsNotification;
  private boolean isActive;
  private Date created;
  private Date updated;

  public User() {}

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
  @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq",
      initialValue = 10, allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", nullable = false)
  public Role getRole() {
    return this.role;
  }

  public void setRole(Role role) {
    this.role = role;
  }


  @Column(name = "username", nullable = false)
  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  @Column(name = "email", unique = true, nullable = false)
  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  @Column(name = "password_hash", nullable = false)
  public String getPasswordHash() {
    return this.passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }
  
  @Column(name = "jwttoken", nullable = false)
  public String getJwtToken() {
    return this.jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  @Column(name = "is_private", nullable = false)
  public boolean isIsPrivate() {
    return this.isPrivate;
  }

  public void setIsPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }


  @Column(name = "wants_notification", nullable = false)
  public boolean isWantsNotification() {
    return this.wantsNotification;
  }

  public void setWantsNotification(boolean wantsNotification) {
    this.wantsNotification = wantsNotification;
  }


  @Column(name = "is_active", nullable = false)
  public boolean isIsActive() {
    return this.isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", insertable = false, updatable = false, nullable = false, length = 29,
      columnDefinition = "TIMESTAMP DEFAULT NOW()")
  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated", length = 29)
  public Date getUpdated() {
    return this.updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

}


