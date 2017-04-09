package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import ch.hsr.adit.application.app.App;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.Spark;
import spark.route.HttpMethod;

public class UserControllerIT {
  
  private Map<String, Object> params = new HashMap<>();

  private int roleId = 1;
  private String username = "junitUser";
  private String email = "studen@hsr.ch";
  private String password = "ultasecure1234";
  private Boolean isPrivate = false;
  private Boolean wantsNotification = true;
  private Boolean isActive = true;
  

  public static void setupClass() {
    App.main(new String[]{});
    Spark.awaitInitialization();
  }

  public static void teardownClass() {
    Spark.stop();
  }
  
  public void setup() {
    params.put("username", username);
    params.put("email", email);
    params.put("passwordHash", password);
    params.put("isPrivate", isPrivate);
    params.put("wantsNotification", wantsNotification);
    params.put("isActive", isActive);
    params.put("role", roleId);
  }

  public void insertUserTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(username, json.get("username"));
    assertEquals(email, json.get("email"));
    assertEquals(password, json.get("passwordHash"));
    assertEquals(isPrivate, (boolean) json.get("isPrivate"));
    assertEquals(wantsNotification, (boolean) json.get("wantsNotification"));
    assertEquals(isActive, (boolean) json.get("isActive"));
    assertNotNull(json.get("role"));
  }
  
  public void insertUserWithMissingFieldsTest() {
    // arrange 
    params.clear();
    params.put("email", email);
    
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("errorCode"));
  }

  public void getUserTest() {
    // arrange
    String expected = "student@hsr.ch";

    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/3", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(expected, json.get("email"));
    assertNotNull(json.get("id"));
  }

  public void getNonExistentUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/1000", null);

    // assert
    Map<String, Object> json = response.json();

    assertEquals(200, response.statusCode);
    assertNotNull(json.get("errorCode"));
  }


  public void getAllUserTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/users", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 3);
  }

  public void updateUserTest() {
    // arrange
    String expectedUpdatedValue = "updated@hsr.ch";
    params.put("email", expectedUpdatedValue);

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/1", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(expectedUpdatedValue, json.get("email"));
    assertNotNull(json.get("id"));
  }
  
  public void updateNonexistentUserTest() {
    // arrange
    params.put("email", "updated@hsr.ch");

    // act
    TestResponse updateResponse = TestUtil.request(HttpMethod.put, "/user/1000", null);

    // assert
    Map<String, Object> json = updateResponse.json();
    assertEquals(200, updateResponse.statusCode);
    assertNotNull(json.get("errorCode"));
  }

  public void deleteUserTest() {
    // act
    TestResponse deleteResponse = TestUtil.request(HttpMethod.delete, "/user/2", null);

    // assert
    Map<String, Object> json = deleteResponse.json();
    assertEquals(200, deleteResponse.statusCode);
    assertEquals(1005, ((Double) json.get("errorCode")).intValue());
  }

  public void deleteInexistentUserTest() {
    // act
    TestResponse deleteResponse = TestUtil.request(HttpMethod.delete, "/user/10000", null);

    // assert
    assertEquals(200, deleteResponse.statusCode);
    assertFalse(Boolean.parseBoolean(deleteResponse.body));
  }

  public void testEmptyUrl() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "", null);
    
    // assert
    assertEquals(404, response.statusCode);
  }
}
