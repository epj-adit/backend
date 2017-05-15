package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.AdvertisementState;
import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.model.Credential;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import ch.hsr.adit.util.TokenUtil;
import spark.route.HttpMethod;

public class AuthenticationControllerIT {

  private User user;
  private Role role;
  private Category category;
  private TokenUtil tokenUtil;
  private String username = "authenticated";
  private String email = "authenticated@hsr.ch";
  private String password = "authentication";
  private Long id = 5l;
  private Boolean isPrivate = false;
  private Boolean wantsNotification = true;
  private Boolean isActive = true;

  @Before
  public void setup() {
    this.role = new Role();
    this.role.setId(1);

    this.category = new Category();
    this.category.setId(1);

    this.tokenUtil = TokenUtil.getInstance();

    this.user = new User();
    this.user.setUsername(username);
    this.user.setEmail(email);
    this.user.setPasswordPlaintext(password);
    this.user.setIsPrivate(isPrivate);
    this.user.setWantsNotification(wantsNotification);
    this.user.setIsActive(isActive);
    this.user.setRole(role);
    this.user.setId(id);
    this.user.setJwtToken(tokenUtil.generateToken(user));
    
    TestUtil.setUseToken(true);
  }


  @Test
  public void testFirstAuthentication() {
    Credential credentials = new Credential();
    credentials.setEmail(email);
    credentials.setPlaintextPassword(password);
    TestResponse response = TestUtil.request(HttpMethod.post, "/authenticate", credentials);
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals("authenticated@hsr.ch", json.get("email"));
    assertNotNull(json.get("id"));
    assertNotNull(json.get("jwtToken"));
  }

  @Test
  public void testFirstAuthenticationWithWrongPassword() {
    Credential credentials = new Credential();
    credentials.setEmail(email);
    credentials.setPlaintextPassword("wrongPassword");
    TestResponse response = TestUtil.request(HttpMethod.post, "/authenticate", credentials);

    assertEquals(401, response.statusCode);
  }

  @Test
  public void testCreateAdvertisementWithoutToken() {
    //disable default testing token
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("auth");
    advertisement.setDescription("auth");
    advertisement.setPrice(100);
    advertisement.setAdvertisementState(AdvertisementState.ACTIVE);
    advertisement.setUser(user);
    advertisement.setCategory(category);

    TestUtil.setTestToken("");
    TestUtil.setUseToken(false);
    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);
    
    assertEquals(401, response.statusCode);
  }
  
  @Test
  public void testCreateAdvertisementWithToken() {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle("auth");
    advertisement.setDescription("auth");
    advertisement.setPrice(100);
    advertisement.setAdvertisementState(AdvertisementState.ACTIVE);
    advertisement.setUser(user);
    advertisement.setCategory(category);
    
    //make sure new valid Token gets created
    TestUtil.setUseToken(true);
    TestUtil.setTestToken(null);
    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", advertisement);
    
    assertEquals(200, response.statusCode);
  }

  @Test
  public void testAuthenticateInactiveUser() {
    Credential credentials = new Credential();
    credentials.setEmail("inactive@hsr.ch");
    credentials.setPlaintextPassword("inactive");
    TestResponse response = TestUtil.request(HttpMethod.post, "/authenticate", credentials);

    assertEquals(401, response.statusCode);
  }
  
  @Test
  public void testNonexistentCredentials() {
    Credential credentials = new Credential();
    credentials.setEmail("nonexistent");
    credentials.setPlaintextPassword("inactive");
    TestResponse response = TestUtil.request(HttpMethod.post, "/authenticate", credentials);

    assertEquals(404, response.statusCode);
  }
  
  @Test
  public void claimTwoTokens() {
    // arrange
    Credential credentials = new Credential();
    credentials.setEmail(email);
    credentials.setPlaintextPassword(password);
    
    // act
    TestResponse response1 = TestUtil.request(HttpMethod.post, "/authenticate", credentials);
    Map<String, Object> json1 = response1.json();
    
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/authenticate", credentials);
    Map<String, Object> json2 = response2.json();
    
    // assert
    String token1 = (String) json1.get("jwtToken");
    String token2 = (String) json2.get("jwtToken");

    assertEquals(token1, token2);
  }
}
