package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.model.Subscription;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.route.HttpMethod;

public class SubscriptionControllerIT {

  private Long interval = new Long(100_000_000);
  private Category category;
  private User user;

  @Before
  public void setup() {
    this.category = new Category();
    this.category.setId(1);

    this.user = new User();
    this.user.setId(1);
  }

  @Test
  public void createSubscription() {
    // arrange
    Subscription subscription = new Subscription();
    subscription.setInterval(interval);
    subscription.setLastUpdated(new Date());
    subscription.setUser(user);
    subscription.setCategory(category);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/subscription", subscription);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(interval, json.get("interval"));
    assertNotNull(json.get("user"));
    assertNotNull(json.get("category"));
  }

  @Test
  public void getSubscription() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/subscription/1", null);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(6_000_000L, json.get("interval"));
    assertNotNull(json.get("id"));
  }

  @Test
  public void getAllFiltered() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get,
        "/subscriptions/?categoryId=1", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 1);
  }

  @Test
  public void getAll() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/subscriptions/", null);

    // assert
    Map<String, Object>[] jsonList = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(jsonList.length >= 1);
  }

  @Test
  public void getNonExistentSubscription() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/subscription/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void updateSubscription() {
    // arrange
    Long updatedValue = 6_000_000L;
    
    Subscription subscription = new Subscription();
    subscription.setId(1);
    subscription.setInterval(updatedValue);
    subscription.setLastUpdated(new Date());
    subscription.setUser(user);
    subscription.setCategory(category);

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/subscription/1", subscription);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(updatedValue, json.get("interval"));
    assertNotNull(json.get("id"));
  }
  
  @Test
  public void updateSubscriptionWithNullUser() {
    // arrange
    Long updatedValue = 6_000_000L;
    
    Subscription subscription = new Subscription();
    subscription.setId(1);
    subscription.setInterval(updatedValue);
    subscription.setLastUpdated(new Date());
    subscription.setUser(null);
    subscription.setCategory(category);

    // act
    TestResponse response = TestUtil.request(HttpMethod.put, "/subscription/2", subscription);
    //fix db
    subscription.setUser(user);
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/subscription/2", subscription);
    // assert
    Map<String, Object> json = response.json();
    assertEquals(409, response.statusCode);
  }

  @Test
  public void deleteSubscription() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/subscription/3", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

  @Test
  public void testIdAutoIncrement() {
    Subscription subscription = new Subscription();
    subscription.setInterval(interval);
    subscription.setLastUpdated(new Date());
    subscription.setUser(user);
    subscription.setCategory(category);
    
    Subscription subscription2 = new Subscription();
    subscription2.setInterval(interval);
    subscription2.setLastUpdated(new Date());
    subscription2.setUser(user);
    subscription2.setCategory(category);

    TestResponse response = TestUtil.request(HttpMethod.post, "/subscription", subscription);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/subscription", subscription2);

    Map<String, Object> json = response.json();
    Map<String, Object> json2 = response2.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertNotNull(json2.get("id"));
    assertEquals((Long) json.get("id") + 1, json2.get("id"));
  }

  @Test
  public void testInsertWithNullUser() {
    Subscription subscription = new Subscription();
    subscription.setInterval(interval);
    subscription.setLastUpdated(new Date());
    subscription.setCategory(category);

    TestResponse response = TestUtil.request(HttpMethod.post, "/subscription", subscription);

    assertEquals(409, response.statusCode);
  }
}
