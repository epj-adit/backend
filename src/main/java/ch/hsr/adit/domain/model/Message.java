package ch.hsr.adit.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "message", schema = "public")
public class Message implements DbEntity {

  private static final long serialVersionUID = 1L;

  private long id;
  private Advertisement advertisement;
  private MessageState messageState;
  private User userBySenderUserId;
  private User userByRecipientUserId;
  private String message;
  private Date created;


  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_seq")
  @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", initialValue = 10,
      allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "advertisement_id")
  public Advertisement getAdvertisement() {
    return this.advertisement;
  }

  public void setAdvertisement(Advertisement advertisement) {
    this.advertisement = advertisement;
  }

  @Enumerated(EnumType.ORDINAL)
  public MessageState getMessageState() {
    return this.messageState;
  }

  public void setMessageState(MessageState messageState) {
    this.messageState = messageState;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sender_user_id")
  public User getUserBySenderUserId() {
    return this.userBySenderUserId;
  }

  public void setUserBySenderUserId(User userBySenderUserId) {
    this.userBySenderUserId = userBySenderUserId;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "recipient_user_id", nullable = false)
  public User getUserByRecipientUserId() {
    return this.userByRecipientUserId;
  }

  public void setUserByRecipientUserId(User userByRecipientUserId) {
    this.userByRecipientUserId = userByRecipientUserId;
  }


  @Column(name = "message", nullable = false)
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
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


