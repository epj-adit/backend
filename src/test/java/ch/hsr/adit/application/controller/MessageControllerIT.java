package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.adit.domain.model.Message;
import ch.hsr.adit.domain.model.MessageState;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.route.HttpMethod;

public class MessageControllerIT {

  private User sender;
  private User recipient;
  private String messageString = "Hallo Welt!";

  @Before
  public void setUp() {
    this.sender = new User();
    this.sender.setId(1);
    this.recipient = new User();
    this.recipient.setId(2);;
  }
  
  @Test
  public void createMessage() {
    // arrange
    Message message = new Message();
    message.setMessage(messageString);
    message.setCreated(new Date());
    message.setMessageState(MessageState.UNREAD);
    message.setAdvertisement(null);
    message.setUserByRecipientUserId(recipient);
    message.setUserBySenderUserId(sender);
    
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/message", message);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(messageString, json.get("message"));
    assertEquals(MessageState.UNREAD.ordinal(),
        Integer.parseInt((String) json.get("messageState")));
    assertNotNull(json.get("userBySenderUserId"));
    assertNotNull(json.get("userByRecipientUserId"));
  }

  @Test
  public void getMessage() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/message/1", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(messageString, json.get("message"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void getAllFiltered() {
    // act
    TestResponse response =
        TestUtil.request(HttpMethod.get, "/messages/?userId=1&userId=2", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 1);
  }

  @Test
  public void getAll() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/messages", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 2);
  }

  @Test
  public void getNonExistentMessage() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/message/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void testIdAutoIncrement() {
    Message message = new Message();
    message.setMessage(messageString);
    message.setCreated(new Date());
    message.setMessageState(MessageState.UNREAD);
    message.setAdvertisement(null);
    message.setUserByRecipientUserId(recipient);
    message.setUserBySenderUserId(sender);

    Message message2 = new Message();
    message2.setMessage(messageString);
    message2.setCreated(new Date());
    message2.setMessageState(MessageState.UNREAD);
    message2.setAdvertisement(null);
    message2.setUserByRecipientUserId(recipient);
    message2.setUserBySenderUserId(sender);

    TestResponse response = TestUtil.request(HttpMethod.post, "/message", message);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/message", message2);

    Map<String, Object> json = response.json();
    Map<String, Object> json2 = response2.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertNotNull(json2.get("id"));
    assertEquals((Long) json.get("id") + 1, json2.get("id"));

  }

  @Test
  public void testInsertWithNullUser() {
    Message message = new Message();
    message.setMessage(messageString);
    message.setCreated(new Date());
    message.setMessageState(MessageState.UNREAD);
    message.setAdvertisement(null);

    TestResponse response = TestUtil.request(HttpMethod.post, "/message", message);

    assertEquals(409, response.statusCode);
  }
}
