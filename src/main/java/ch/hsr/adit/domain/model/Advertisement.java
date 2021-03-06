package ch.hsr.adit.domain.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;



@Data
@Entity
@Table(name = "advertisement", schema = "public")
public class Advertisement implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private AdvertisementState advertisementState;
  private Category category;
  private User user;
  private String title;
  private String description;
  private int price;
  private Date created;
  private Date updated;
  private Set<Tag> tags = new HashSet<>(0);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advert_id_seq")
  @SequenceGenerator(name = "advert_id_seq", sequenceName = "advert_id_seq", initialValue = 10,
      allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  public AdvertisementState getAdvertisementState() {
    return this.advertisementState;
  }

  public void setAdvertisementState(AdvertisementState advertisementState) {
    this.advertisementState = advertisementState;
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


  @Column(name = "title", nullable = false)
  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  @Column(name = "description", nullable = false)
  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Column(name = "price", nullable = false)
  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
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

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "advertisement_tag", schema = "public",
      joinColumns = { @JoinColumn(name = "advertisement_id", nullable = false, updatable = false) },
      inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable = false, updatable = false) })
  public Set<Tag> getTags() {
    return this.tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

}


