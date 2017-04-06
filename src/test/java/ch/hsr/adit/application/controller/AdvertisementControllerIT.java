package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import ch.hsr.adit.application.app.App;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.Spark;
import spark.route.HttpMethod;

public class AdvertisementControllerIT {
  
  private Map<String, Object> params = new HashMap<>();

  private String title = "Betriebssysteme";
  private String description = "Bsys2, Eduard Glatz";
  private int price = 1000;
  private int userId = 1;
  private int categoryId = 1;

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
    params.put("title", title);
    params.put("description", description);
    params.put("price", price);
    params.put("categoryId", categoryId);
    params.put("userId", userId);
  }

  public void insertUserTest() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/advertisement", params);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals(title, json.get("title"));
    assertEquals(description, json.get("description"));
    assertEquals(price, json.get("price"));
    assertNotNull(json.get("user"));
    assertNotNull(json.get("category"));
  }
  
 
}
