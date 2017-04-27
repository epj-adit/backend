package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.gson.reflect.TypeToken;

import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import ch.hsr.adit.util.JsonUtil;
import spark.route.HttpMethod;

public class PermissionControllerIT {

  @Test
  public void createPermission() {
    // arrange
    String name = new String("All");
    Permission permission = new Permission();
    permission.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/permission", permission);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
  }

  @Test
  public void getPermissionById() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/permission/1", null);

    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals("create_review", json.get("name"));
  }

  @Test
  public void getNonExistentPermission() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/permission/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void deletePermission() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/permission/2", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

  @Test
  public void createPermissionTwice() {
    // arrange
    String name = new String("Conflict");
    Permission permission = new Permission();
    permission.setName(name);

    Permission permission2 = new Permission();
    permission2.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/permission", permission);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/permission", permission2);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
    assertEquals(409, response2.statusCode);
  }

  @Test
  public void createPermissionWithNullName() {
    Permission permission = new Permission();
    permission.setName(null);

    TestResponse response = TestUtil.request(HttpMethod.post, "/permission", permission);

    assertEquals(409, response.statusCode);
  }

  @Test
  public void updatePermissionWithNullName() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/permission/3", null);

    Permission permission =
        JsonUtil.fromJson(response.body, new TypeToken<Permission>() {}.getType());
    permission.setName(null);
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/permission/3", permission);

    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals("to_update", json.get("name"));
    assertEquals(409, response2.statusCode);
  }

}
