package ch.hsr.adit.domain.model;


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
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "category", schema = "public",
    uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private Category parentCategory;
  private String name;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
  @SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq", initialValue = 10,
      allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_category_id")
  public Category getParentCategory() {
    return this.parentCategory;
  }

  public void setParentCategory(Category parentCategory) {
    this.parentCategory = parentCategory;
  }


  @Column(name = "name", unique = true, nullable = false)
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

}


