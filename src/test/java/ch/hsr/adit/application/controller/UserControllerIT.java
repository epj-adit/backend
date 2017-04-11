package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
  private String passwordHash = "ultasecure1234";
  private Boolean isPrivate = false;
  private Boolean wantsNotification = true;
  private Boolean isActive = true;

  @Before
  public void setup() {
    this.role = new Role();
    this.role.setId(1);
  }

  @Test
  public void createUser() {
    // arrange
    User user = new User();
    user.setUsername(username);
    user.setEmail("new.student@hsr.ch");
    user.setPasswordHash(passwordHash);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", user);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(username, json.get("username"));
    assertEquals("new.student@hsr.ch", json.get("email"));
    assertEquals(passwordHash, json.get("passwordHash"));
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
    TestResponse response = TestUtil.request(HttpMethod.get, "/users", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 3);
  }


  @Test
  public void updateUserTest() {
    // arrange
    User user = new User();
    user.setId(1);
    user.setUsername(username);
    user.setEmail("updatedStudent@hsr.ch");
    user.setPasswordHash(passwordHash);
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
    assertNotNull(json.get("id"));
  }

  @Test
  public void deleteUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/user/4", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

  @Test
  public void testIdAutoIncerement() {
    User user = new User();
    user.setUsername(username);
    user.setEmail("newer.student@hsr.ch");
    user.setPasswordHash(passwordHash);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    User user2 = new User();
    user2.setUsername(username);
    user2.setEmail("newerer.student@hsr.ch");
    user2.setPasswordHash(passwordHash);
    user2.setIsPrivate(isPrivate);
    user2.setWantsNotification(wantsNotification);
    user2.setIsActive(isActive);
    user2.setRole(role);

    TestResponse response = TestUtil.request(HttpMethod.post, "/user", user);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/user", user2);

    Map<String, Object> json = response.json();
    Map<String, Object> json2 = response2.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertNotNull(json2.get("id"));
    assertEquals((Long) json.get("id") + 1, json2.get("id"));
  }

  @Test
  public void deleteReferencedUserTest() {
    TestResponse response = TestUtil.request(HttpMethod.delete, "/user/1", null);

    assertEquals(404, response.statusCode);
    assertFalse(Boolean.parseBoolean(response.body));
  }

  @Test
  public void failedUpdateUserTest() {
    // arrange
    User user = new User();
    user.setId(1);
    user.setUsername(username);
    user.setEmail("updatedStudent@hsr.ch");
    user.setPasswordHash(passwordHash);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    String updatedValue = "nvinzens@hsr.ch";


    // act
    user.setEmail(updatedValue);
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/1", user);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(404, response.statusCode);
  }

  @Test
  public void insertUserTwiceTest() {
    User user = new User();
    user.setUsername(username);
    user.setEmail("duplicate.student@hsr.ch");
    user.setPasswordHash(passwordHash);
    user.setIsPrivate(isPrivate);
    user.setWantsNotification(wantsNotification);
    user.setIsActive(isActive);
    user.setRole(role);

    User user2 = new User();
    user2.setUsername(username);
    user2.setEmail("duplicate.student@hsr.ch");
    user2.setPasswordHash(passwordHash);
    user2.setIsPrivate(isPrivate);
    user2.setWantsNotification(wantsNotification);
    user2.setIsActive(isActive);
    user2.setRole(role);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", user);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/user", user2);

    // assert
    assertEquals(200, response.statusCode);
    assertEquals(404, response2.statusCode);
  }

}
