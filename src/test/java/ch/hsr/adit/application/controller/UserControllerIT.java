package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.hsr.adit.application.app.App;
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

  @BeforeClass
  public static void setupClass() {
    App.main(new String[] {});
  }

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
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("errorCode"));
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
    TestResponse response = TestUtil.request(HttpMethod.delete, "/user/0", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

}
