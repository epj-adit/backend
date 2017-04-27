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

import lombok.Data;

@Data
@Entity
@Table(name = "user_log", schema = "public")
public class UserLog implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private User user;
  private String ip;
  private String action;
  private Date created;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_id_seq")
  @SequenceGenerator(name = "log_id_seq", sequenceName = "log_id_seq",
      initialValue = 10, allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  @Column(name = "ip", nullable = false)
  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }


  @Column(name = "action", nullable = false)
  public String getAction() {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
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



}


