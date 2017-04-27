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

import lombok.Data;

@Data
@Entity
@Table(name = "media", schema = "public")
public class Media implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private Advertisement advertisement;
  private String filename;
  private String description;
  private byte[] media;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_id_seq")
  @SequenceGenerator(name = "media_id_seq", sequenceName = "media_id_seq", initialValue = 10,
      allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "advertisement_id", nullable = false)
  public Advertisement getAdvertisement() {
    return this.advertisement;
  }

  public void setAdvertisement(Advertisement advertisement) {
    this.advertisement = advertisement;
  }


  @Column(name = "filename", nullable = false)
  public String getFilename() {
    return this.filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }


  @Column(name = "description")
  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Column(name = "media", nullable = false)
  public byte[] getMedia() {
    return this.media;
  }

  public void setMedia(byte[] media) {
    this.media = media;
  }



}


