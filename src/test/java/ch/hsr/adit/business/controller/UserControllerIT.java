package ch.hsr.adit.business.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.hsr.adit.app.App;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.Spark;
import spark.route.HttpMethod;

public class UserControllerIT {

  @BeforeClass
  public static void setup() {
    App.main(null);
    Spark.awaitInitialization();
  }

  @AfterClass
  public static void teardown() {
    Spark.stop();
  }

  @Test
  public void insertUserTest() {
    // arrange
    String valueToInsert = "studen@hsr.ch";
    Map<String, Object> params = new HashMap<>();
    params.put("email", valueToInsert);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/user", params);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(valueToInsert, json.get("email"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void getUserTest() {
    // arrange
    String expected = "student@hsr.ch";

    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/user/1", null);

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
    assertEquals(200, response.statusCode);
    assertNull(response.json());
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
    Map<String, Object> params = new HashMap<>();
    params.put("email", "updated@hsr.ch");

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/user/3", params);

    // assert
    Map<String, String> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(expectedUpdatedValue, json.get("email"));
    assertNotNull(json.get("id"));
  }
  
  @Test()
  public void updateNonexistentUserTest() {
    // arrange
    Map<String, Object> params = new HashMap<>();
    params.put("email", "updated@hsr.ch");

    // act
    TestResponse updateResponse = TestUtil.request(HttpMethod.put, "/user/5", params);

    // assert
    Map<String, String> json = updateResponse.json();
    assertEquals(200, updateResponse.statusCode);
    assertNotNull(json.get("message"));
  }

  @Test
  public void deleteUserTest() {
    // act
    TestResponse deleteResponse = TestUtil.request(HttpMethod.delete, "/user/2", null);

    // assert
    assertEquals(200, deleteResponse.statusCode);
    assertTrue(Boolean.parseBoolean(deleteResponse.body));
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
    Map<String, String> json = response.json();
    assertEquals(404, response.statusCode);
  }
}
