package ch.hsr.adit.application.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
  

  @BeforeClass
  public static void setupClass() {
    App.main(new String[]{});
    Spark.awaitInitialization();
  }

  @AfterClass
  public static void teardownClass() {
    Spark.stop();
  }
  
  @Before
  public void setup() {
    params.put("username", username);
    params.put("email", email);
    params.put("passwordHash", password);
    params.put("isPrivate", isPrivate);
    params.put("wantsNotification", wantsNotification);
    params.put("isActive", isActive);
    params.put("role", roleId);
  }

  @Test
  public void insertUserTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", params);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(username, json.get("username"));
    assertEquals(email, json.get("email"));
    assertEquals(password, json.get("passwordHash"));
    assertEquals(isPrivate, json.get("isPrivate"));
    assertEquals(wantsNotification, json.get("wantsNotification"));
    assertEquals(isActive, json.get("isActive"));
    assertNotNull(json.get("role"));
  }
  
  @Test
  public void insertUserWithMissingFieldsTest() {
    // arrange 
    params.clear();
    params.put("email", email);
    
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", params);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("errorCode"));
  }

  @Test
  public void getUserTest() {
    // arrange
    String expected = "student@hsr.ch";

    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/3", null);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(expected, json.get("email"));
    assertNotNull(json.get("id"));
  }

  @Test()
  public void getNonExistentUser() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/1000", null);

    // assert
    Map<String, String> json = response.json();

    assertEquals(200, response.statusCode);
    assertNotNull(json.get("errorCode"));
  }


  @Test
  public void getAllUserTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/users", null);

    // asert
    Map<String, String>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertThat(jsonList.length, greaterThanOrEqualTo(3));
  }

  @Test
  public void updateUserTest() {
    // arrange
    String expectedUpdatedValue = "updated@hsr.ch";
    params.put("email", expectedUpdatedValue);

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/1", params);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(expectedUpdatedValue, json.get("email"));
    assertNotNull(json.get("id"));
  }
  
  @Test()
  public void updateNonexistentUserTest() {
    // arrange
    params.put("email", "updated@hsr.ch");

    // act
    TestResponse updateResponse = TestUtil.request(HttpMethod.put, "/user/5", params);

    // assert
    Map<String, String> json = updateResponse.json();
    assertEquals(200, updateResponse.statusCode);
    assertNotNull(json.get("errorCode"));
  }

  @Test
  public void deleteUserTest() {
    // act
    TestResponse deleteResponse = TestUtil.request(HttpMethod.delete, "/user/2", null);

    // assert
    Map<String, String> json = deleteResponse.json();
    assertEquals(200, deleteResponse.statusCode);
    assertEquals(1005.0, json.get("errorCode"));
  }

  @Test
  public void deleteInexistentUserTest() {
    // act
    TestResponse deleteResponse = TestUtil.request(HttpMethod.delete, "/user/10000", null);

    // assert
    assertEquals(200, deleteResponse.statusCode);
    assertFalse(Boolean.parseBoolean(deleteResponse.body));
  }

  @Test()
  public void testEmptyUrl() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "", null);
    
    // assert
    assertEquals(404, response.statusCode);
  }
}
