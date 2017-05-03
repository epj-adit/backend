package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.gson.reflect.TypeToken;

import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import ch.hsr.adit.util.JsonUtil;
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
  public void getCategoryById() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/category/1", null);

    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertNotNull(json.get("id"));
    assertEquals("Bücher", json.get("name"));
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
  public void getAllCategory() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/categories/", null);

    // assert
    Map<String, Object>[] json = response.jsonList();
    assertEquals(200, response.statusCode);
    assertTrue(json.length >= 3);
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
    TestResponse response = TestUtil.request(HttpMethod.delete, "/category/4", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

  @Test
  public void createCategoryTwice() {
    // arrange
    String name = new String("Conflict");
    Category category = new Category();
    category.setName(name);

    Category category2 = new Category();
    category2.setName(name);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/category", category);
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/category", category2);

    // assert
    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals(name, json.get("name"));
    assertEquals(409, response2.statusCode);
  }

  @Test
  public void createCategoryWithNullName() {
    Category category = new Category();
    category.setName(null);

    TestResponse response = TestUtil.request(HttpMethod.post, "/category", category);

    assertEquals(409, response.statusCode);
  }

  @Test
  public void updateCategoryWithNullName() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/category/2", null);

    Category category =
        JsonUtil.fromJson(response.body, new TypeToken<Category>() {}.getType());
    category.setName(null);
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/category/2", category);

    Map<String, Object> json = response.json();
    assertEquals(200, response.statusCode);
    assertEquals("Jobs", json.get("name"));
    assertEquals(409, response2.statusCode);
  }
  
  @Test
  public void updateCategory() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/category/2", null);

    Category category =
        JsonUtil.fromJson(response.body, new TypeToken<Category>() {}.getType());
    category.setName("updated");
    TestResponse response2 = TestUtil.request(HttpMethod.put, "/category/2", category);

    assertEquals(200, response.statusCode);
    assertEquals(200, response2.statusCode);
  }
  
  @Test
  public void testDeleteOnReferencedCategory() {
    TestResponse response = TestUtil.request(HttpMethod.get, "/category/1", null);

    Category parent =
        JsonUtil.fromJson(response.body, new TypeToken<Category>() {}.getType());
    Category child = new Category();
    child.setName("Child");
    child.setParentCategory(parent);
    
    TestResponse response2 = TestUtil.request(HttpMethod.post, "/category", child);
    TestResponse response3 = TestUtil.request(HttpMethod.delete, "/category/1", null);
    
    assertEquals(200, response.statusCode);
    assertEquals(200, response2.statusCode);
    assertEquals(409, response3.statusCode);
  }

}
