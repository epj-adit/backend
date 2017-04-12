package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.route.HttpMethod;

public class CategoryControllerIT {

  @Test
  public void createCategory() {
    // arrange
    String name = new String("Gadgets");
    Category category = new Category();
    category.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/category", category);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
  }

  @Test
  public void getCategoryFiltered() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/categories/?name=b", null);

    // assert
    Map<String, Object>[] json = response.jsonList();
    assertEquals(200, response.statusCode);
    assertNotNull(json[0].get("id"));
    assertEquals("Bücher", json[0].get("name"));
  }

  @Test
  public void getNonExistentCategory() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/category/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void deleteCategory() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/category/2", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

}
