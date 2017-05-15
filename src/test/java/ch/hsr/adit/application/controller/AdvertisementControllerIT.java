package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.AdvertisementState;
import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import ch.hsr.adit.util.PermissionUtil;
import ch.hsr.adit.util.TokenUtil;
import spark.route.HttpMethod;

public class AdvertisementControllerIT {

  private Category category;
  private User user;
  private Role role;

  private String title = "Betriebsysteme";
  private String description = "Ein Buch von Eduard Glatz für die Vorlesung Bsys1 und Bsys2";
  private int price = 1000;
  private AdvertisementState advertisementState = AdvertisementState.ACTIVE;

  @Before
  public void setup() {
    TestUtil.setUseToken(true);
    this.category = new Category();
    this.category.setId(1);
    
    role = new Role();
    Permission permission = PermissionUtil.BASIC_PERMISSION;
    Set<Permission> permissions = new HashSet<>();
    permissions.add(permission);
    role.setPermissions(permissions);
    
    this.user = new User();
    this.user.setUsername("student@hsr.ch");
    this.user.setId(3);
    this.user.setRole(role);
  }

  @Test
  public void createAdvertisement() {
    // arrange
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle(title);
    advertisement.setDescription(description);
    advertisement.setPrice(price);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(user);
    advertisement.setCategory(category);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(title, json.get("title"));
    assertEquals(advertisementState.ordinal(),
        Integer.parseInt((String) json.get("advertisementState")));
    assertEquals(description, json.get("description"));
    assertEquals(price, ((Long) json.get("price")).intValue());
    assertNotNull(json.get("user"));
    assertNotNull(json.get("category"));
  }

  @Test
  public void getAdvertisement() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/advertisement/1", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(title, json.get("title"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void getAllFiltered() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get,
        "/advertisements/?title=Be&description=B&userId=3&tagId=1&tagId=2&categoryId=1", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 1);
  }

  @Test
  public void getFilteredAndOrTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get,
        "/advertisements/?title=B&description=B&categoryId=1&advertisementState=2", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 2);
  }

  @Test
  public void getAllFilteredByTag() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/advertisements/?tagId=1", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length == 2);
  }

  @Test
  public void getAllFilteredTitleDescription() {
    // act
    TestResponse response =
        TestUtil.request(HttpMethod.get, "/advertisements/?title=Be&description=B", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 1);
  }

  @Test
  public void getAllFilteredByState() {
    // act: get all active and to_review
    TestResponse response = TestUtil.request(HttpMethod.get,
        "/advertisements/?advertisementState=2&advertisementState=0", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 4);
  }

  @Test
  public void getAll() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/advertisements/", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 3);
  }

  @Test
  public void getNonExistentAdvertisement() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/advertisement/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void updateAdvertisement() {
    // arrange
    String updatedValue = "GoF Patterns";
    Advertisement advertisement = new Advertisement();
    advertisement.setId(1);
    advertisement.setTitle(updatedValue);
    advertisement.setDescription(description);
    advertisement.setPrice(price);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(user);
    advertisement.setCategory(category);

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/advertisement/5", advertisement);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(updatedValue, json.get("title"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void updateNonExistentAdvertisement() {
    String updatedValue = "GoF Patterns";
    Advertisement advertisement = new Advertisement();
    advertisement.setId(1);
    advertisement.setTitle(updatedValue);
    advertisement.setDescription(description);
    advertisement.setPrice(price);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(user);
    advertisement.setCategory(category);
    
    TestResponse response =
        TestUtil.request(HttpMethod.put, "/advertisement/10000001", advertisement);

    assertEquals(404, response.statusCode);
  }

  @Test
  public void deleteAdvertisement() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/advertisement/4", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(AdvertisementState.CLOSED.ordinal(),
        Integer.parseInt((String) json.get("advertisementState")));
  }

  @Test
  public void testIdAutoIncrement() {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("123");
    advertisement.setDescription(description);
    advertisement.setPrice(price);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(user);
    advertisement.setCategory(category);

    Advertisement advertisement2 = new Advertisement();
    advertisement2.setTitle("234");
    advertisement2.setDescription(description);
    advertisement2.setPrice(price);
    advertisement2.setAdvertisementState(advertisementState);
    advertisement2.setUser(user);
    advertisement2.setCategory(category);

    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/advertisement", advertisement2);

    Map<String, Object> json = response.json();
    Map<String, Object> json2 = response2.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertNotNull(json2.get("id"));
    assertEquals((Long) json.get("id") + 1, json2.get("id"));

  }

  @Test
  public void testInsertWithNullUser() {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle(title);
    advertisement.setDescription(description);
    advertisement.setPrice(price);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(null);
    advertisement.setCategory(category);

    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);

    assertEquals(409, response.statusCode);
  }

  @Test
  public void testInsertWithManualId() {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("abcd");
    advertisement.setDescription("abcd");
    advertisement.setPrice(10000);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setUser(user);
    advertisement.setCategory(category);
    advertisement.setId(3);
    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);

    assertEquals(200, response.statusCode);
  }

  @Test
  public void updateAdThatUserDoesntOwn() {
    TestUtil.setUseToken(false);
    
    //user needs any random permission so theres no nullpointerexceptions
    Set<Permission> permissions = new HashSet<>();
    Permission perm = new Permission();
    perm.setId(1);
    perm.setName("review");
    Permission perm2 = new Permission();
    perm2.setId(2);
    perm2.setName("ban");
    permissions.add(perm);
    permissions.add(perm2);

    Role role = new Role();
    role.setId(2);
    role.setPermissions(permissions);
    role.setName("bla");

    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("abcd");
    advertisement.setDescription("abcd");
    advertisement.setPrice(10000);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setCategory(category);
    advertisement.setId(3);

    User newUser = new User();
    newUser.setEmail("illegaluser@hsr.ch");
    newUser.setId(2);
    newUser.setRole(role);

    TokenUtil tokenUtil = TokenUtil.getInstance();
    String token = tokenUtil.generateToken(newUser);
    newUser.setJwtToken(token);
    advertisement.setUser(newUser);

    TestResponse response = TestUtil.request(HttpMethod.put, "/advertisement/1", advertisement);

    assertEquals(403, response.statusCode);
    TestUtil.setUseToken(true);

  }
  
  @Test
  public void deleteAdThatUserDoesntOwn() {
    TestUtil.setNoPermissionsUser(true);
    TestUtil.setTestToken(null);
    Set<Permission> permissions = new HashSet<>();
    Permission perm = new Permission();
    perm.setId(1);
    perm.setName("review");
    Permission perm2 = new Permission();
    perm2.setId(2);
    perm2.setName("ban");
    permissions.add(perm);
    permissions.add(perm2);

    Role role = new Role();
    role.setId(2);
    role.setPermissions(permissions);
    role.setName("bla");

    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("abcd");
    advertisement.setDescription("abcd");
    advertisement.setPrice(10000);
    advertisement.setAdvertisementState(advertisementState);
    advertisement.setCategory(category);
    advertisement.setId(3);

    User newUser = new User();
    newUser.setEmail("illegaluser@hsr.ch");
    newUser.setId(2);
    newUser.setRole(role);

    TokenUtil tokenUtil = TokenUtil.getInstance();
    String token = tokenUtil.generateToken(newUser);
    newUser.setJwtToken(token);
    advertisement.setUser(newUser);

    TestResponse response = TestUtil.request(HttpMethod.delete, "/advertisement/2", null);

    assertEquals(403, response.statusCode);
    TestUtil.setUseToken(true);
  }
}
