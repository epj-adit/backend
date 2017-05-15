package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import ch.hsr.adit.util.JsonUtil;
import spark.route.HttpMethod;

public class RoleControllerIT {

  @Before
  public void setup() {
    TestUtil.setUseToken(true);
    TestUtil.setNoPermissionsUser(false);
    TestUtil.setTestToken(null);
  }
  
  @Test
  public void createRole() {
    // arrange
    String name = new String("Gangster");
    Role role = new Role();
    role.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/role", role);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
  }
  
  @Test
  public void createWithoutEditRolePermission() {
    TestUtil.setNoPermissionsUser(true);
    
    String name = new String("no permission");
    Role role = new Role();
    role.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/role", role);

    // assert
    assertEquals(403, response.statusCode);
  }

  @Test
  public void getRoleById() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/role/1", null);

    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals("admin", json.get("name"));
  }

  @Test
  public void getNonExistentRole() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/role/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void deleteRole() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/role/4", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }
  
  @Test
  public void deleteWithoutEditRolePermission() {
    TestUtil.setNoPermissionsUser(true);
    
    TestResponse response = TestUtil.request(HttpMethod.delete, "/role/3", null);

    // assert
    assertEquals(403, response.statusCode);
  }

  @Test
  public void createRoleTwice() {
    // arrange
    String name = new String("Conflict");
    Role role = new Role();
    role.setName(name);

    Role role2 = new Role();
    role2.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/role", role);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/role", role2);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
    assertEquals(409, response2.statusCode);
  }

  @Test
  public void createRoleWithNullName() {
    Role role = new Role();
    role.setName(null);

    TestResponse response = TestUtil.request(HttpMethod.post, "/role", role);

    assertEquals(409, response.statusCode);
  }

  @Test
  public void updateRoleWithNullName() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/role/2", null);

    Role role = JsonUtil.fromJson(response.body, new TypeToken<Role>() {}.getType());
    role.setName(null);
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/role/2", role);

    assertEquals(200, response.statusCode);
    assertEquals(409, response2.statusCode);
  }
  
  @Test
  public void updateRole() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/role/2", null);

    Role role = JsonUtil.fromJson(response.body, new TypeToken<Role>() {}.getType());
    role.setName("updated");
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/role/2", role);

    Map<String, Object> json = response2.json();
    assertEquals(200, response.statusCode);
    assertEquals("updated", json.get("name"));
    assertEquals(200, response2.statusCode);
  }
  
  @Test
  public void updateWithoutEditRolePermission() {
    TestUtil.setNoPermissionsUser(true);
    
    TestResponse response = TestUtil.request(HttpMethod.get, "/role/2", null);

    Role role = JsonUtil.fromJson(response.body, new TypeToken<Role>() {}.getType());
    role.setName("updated");
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/role/2", role);

    assertEquals(200, response.statusCode);
    assertEquals(403, response2.statusCode);
  }

  @Test
  public void deleteReferencedRole() {
    TestResponse response = TestUtil.request(HttpMethod.delete, "/role/2", null);

    // assert
    assertEquals(409, response.statusCode);
  }
}
