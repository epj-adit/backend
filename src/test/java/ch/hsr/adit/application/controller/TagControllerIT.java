package ch.hsr.adit.application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.hsr.adit.domain.model.Tag;
import ch.hsr.adit.test.TestResponse;
import ch.hsr.adit.test.TestUtil;
import spark.route.HttpMethod;

public class TagControllerIT {

  @Test
  public void createTags() {
    // arrange
    Tag tag = new Tag();
    tag.setName("GoF");

    Tag tag2 = new Tag();
    tag2.setName("Informatik");

    List<Tag> tags = new ArrayList<>();
    tags.add(tag);
    tags.add(tag2);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/tags/", tags);

    // assert
    Map<String, Object>[] json = response.jsonList();
    assertEquals(200, response.statusCode);
    assertEquals(2, json.length);
  }

  @Test
  public void createDuplicateTags() {
    // arrange
    String name = "GoF";
    Tag tag = new Tag();
    tag.setName(name);
    Tag tag2 = new Tag();
    tag2.setName(name);

    List<Tag> tags = new ArrayList<>();
    tags.add(tag);
    tags.add(tag2);

    // act
    TestResponse response = TestUtil.request(HttpMethod.post, "/tags/", tags);

    // assert
    Map<String, Object>[] json = response.jsonList();
    assertEquals(200, response.statusCode);
    assertEquals(2, json.length);
  }

  @Test
  public void getTag() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/tags/?name=ed", null);

    // assert
    Map<String, Object>[] json = response.jsonList();
    assertEquals(200, response.statusCode);
    assertNotNull(json[0].get("id"));
    assertEquals("Eduard Glatz", json[0].get("name"));
  }

  @Test
  public void getNonExistentTag() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.get, "/tag/10000000", null);

    // assert
    assertEquals(404, response.statusCode);
  }

  @Test
  public void deleteTag() {
    // act
    TestResponse response = TestUtil.request(HttpMethod.delete, "/tag/2", null);

    // assert
    assertEquals(200, response.statusCode);
    assertTrue(Boolean.parseBoolean(response.body));
  }

  @Test
  public void createNullTag() {
    Tag tag = new Tag();
    tag.setName(null);

    List<Tag> tags = new ArrayList<>();
    tags.add(tag);
    TestResponse response = TestUtil.request(HttpMethod.post, "/tags/", tags);

    assertEquals(409, response.statusCode);
  }

}
