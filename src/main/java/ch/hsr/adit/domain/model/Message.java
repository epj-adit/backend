package ch.hsr.adit.domain.model;
// Generated 23.03.2017 11:05:29 by Hibernate Tools 5.2.1.Final


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Message generated by hbm2java
 */
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "message_state_id")
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


