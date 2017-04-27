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
@Table(name = "subscription", schema = "public")
public class Subscription implements DbEntity {
  
  private static final long serialVersionUID = 1L;

  private long id;
  private Category category;
  private User user;
  private long interval;
  private Date lastUpdated;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_id_seq")
  @SequenceGenerator(name = "subscription_id_seq", sequenceName = "subscription_id_seq",
      initialValue = 10, allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", nullable = false)
  public Category getCategory() {
    return this.category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  @Column(name = "interval", nullable = false)
  public long getInterval() {
    return this.interval;
  }

  public void setInterval(long interval) {
    this.interval = interval;
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_updated", nullable = false, length = 29)
  public Date getLastUpdated() {
    return this.lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }



}

