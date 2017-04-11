package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Tag;
import ch.hsr.adit.domain.persistence.TagDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class TagService {

  private static final Logger LOGGER = Logger.getLogger(TagService.class);

  private final TagDao tagDao;

  public TagService(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  public Tag createTag(Tag tag) {
    return (Tag) tagDao.persist(tag);
  }

  public Tag updateTag(Tag tag) {
    return tagDao.update(tag);
  }

  public boolean deleteTag(Tag tagToDelete) {
    tagDao.delete(tagToDelete);
    return true;
  }

  public boolean deleteTag(long id) {
    Tag tag = get(id);
    deleteTag(tag);
    return true;
  }

  public Tag get(Tag tag) {
    return get(tag.getId());
  }

  public Tag get(Long id) {
    Tag tag = tagDao.get(id);
    return tag;
  }

  public List<Tag> getAll() {
    return tagDao.getAll();
  }

  public Tag transformToTag(Request request) {
    try {
      Tag tag = JsonUtil.fromJson(request.body(), Tag.class);
      LOGGER.info("Received JSON data: " + tag.toString());
      return tag;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
