package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.route.HttpMethod;

public class UserControllerIT {

  private Role role;
  private String username = "junitUser";
  private String password = "ultasecure1234";
  private Boolean isPrivate = false;
  private Boolean wantsNotification = true;
  private Boolean isActive = true;

  @Before
  public void setup() {
    this.role = new Role();
    this.role.setId(1);

    TestUtil.setUseToken(true);
  }

  @Test
  public void createUser() {
    // arrange
    User user = new User();
    user.setUsername(username);
    user.setEmail("new.student@hsr.ch");
    user.setPasswordPlaintext(password);
    user.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    TestUtil.setUseToken(false);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/register", user);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(username, json.get("username"));
    assertEquals("new.student@hsr.ch", json.get("email"));
    assertNull(json.get("passwordHash"));
    assertNull(json.get("passwortPlaintext"));
    assertNotNull(json.get("jwtToken"));
    assertEquals(isPrivate, (Boolean) json.get("isPrivate"));
    assertEquals(wantsNotification, (Boolean) json.get("wantsNotification"));
    assertEquals(isActive, (Boolean) json.get("isActive"));
    assertNotNull(json.get("role"));
  }


  @Test
  public void getUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/3", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals("student@hsr.ch", json.get("email"));
    assertNull(json.get("passwordHash"));
    assertNull(json.get("passwortPlaintext"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void getNonExistentUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }


  @Test
  public void getAllUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/users/", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 3);
  }

  @Test
  public void getUserByConversation() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/users/?conversationUserId=1", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertEquals(2, jsonList.length);
  }

  @Test
  public void updateUserTest() {
    // arrange
    User user = new User();
    user.setId(1);
    user.setUsername(username);
    user.setEmail("updatedStudent@hsr.ch");
    user.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user.setPasswordPlaintext(password);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    String updatedValue = "Niguaran";

    // act
    user.setUsername(updatedValue);
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/1", user);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(updatedValue, json.get("username"));
    assertNull(json.get("passwordHash"));
    assertNull(json.get("passwortPlaintext"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void deleteUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/user/4", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertFalse((Boolean) json.get("isActive"));
  }

  @Test
  public void testIdAutoIncerement() {
    User user = new User();
    user.setUsername(username);
    user.setEmail("newer.student@hsr.ch");
    user.setPasswordPlaintext(password);
    user.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    User user2 = new User();
    user2.setUsername(username);
    user2.setEmail("newerer.student@hsr.ch");
    user2.setPasswordPlaintext(password);
    user2.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user2.setIsPrivate(isPrivate);
    user2.setWantsNotification(wantsNotification);
    user2.setIsActive(isActive);
    user2.setRole(role);

    TestResponse response = TestUtil.request(HttpMethod.post, "/register", user);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/register", user2);

    Map<String, Object> json = response.json();
    Map<String, Object> json2 = response2.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertNotNull(json2.get("id"));
    assertEquals((Long) json.get("id") + 1, json2.get("id"));
  }

  @Test
  public void duplicateEmailUpdateTest() {
    // arrange
    User user = new User();
    user.setId(1);
    user.setUsername(username);
    user.setEmail("updatedStudent@hsr.ch");
    user.setPasswordPlaintext(password);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    String updatedValue = "nvinzens@hsr.ch";


    // act
    user.setEmail(updatedValue);
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/1", user);

    // assert
    assertEquals(409, response.statusCode);
  }

  @Test
  public void insertUserWithPut() {
    // arrange
    User user = new User();
    user.setEmail("update@hsr.ch");

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/10000000", user);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void deleteNonexistentUserTest() {

    TestResponse response = TestUtil.request(HttpMethod.delete, "/user/1000000", null);

    // assert
    assertEquals(404, response.statusCode);
    assertFalse(Boolean.parseBoolean(response.body));
  }

  @Test
  public void insertUserTwiceTest() {
    User user = new User();
    user.setUsername(username);
    user.setEmail("duplicate.student@hsr.ch");
    user.setPasswordPlaintext(password);
    user.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    User user2 = new User();
    user2.setUsername(username);
    user2.setEmail("duplicate.student@hsr.ch");
    user2.setPasswordPlaintext(password);
    user2.setJwtToken(
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGFkaXQuY2giLCJwZXJtaXNzaW9"
        + "ucyI6WyJ0ZXN0X3Blcm1pc3Npb24iXSwiaXNzIjoiYWRpdCIsImV4cCI6MTQ5N"
        + "DU3NTgwM30.jtrFwDxg9CoyjtUYaUjG1FRqdu-cv3NdhCjBM44zBec");
    user2.setIsPrivate(isPrivate);
    user2.setWantsNotification(wantsNotification);
    user2.setIsActive(isActive);
    user2.setRole(role);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/register", user);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/register", user2);

    // assert
    assertEquals(200, response.statusCode);
    assertEquals(409, response2.statusCode);
  }

  @Test
  public void insertUserWithNullField() {
    User user = new User();
    user.setUsername(username);
    user.setEmail("nullUser.student@hsr.ch");
    user.setPasswordPlaintext(null);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    TestResponse response = TestUtil.request(HttpMethod.post, "/register", user);

    assertEquals(409, response.statusCode);
  }

}
