package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.exception.UserError;
import ch.hsr.adit.domain.model.Tag;
import ch.hsr.adit.domain.persistence.TagDao;
import spark.Request;


public class TagService {

  private static final Logger LOGGER = Logger.getLogger(TagService.class);
  
  private final TagDao tagDao;

  public TagService(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  public Tag createTag(Tag tag) {
    try {
      return (Tag) tagDao.persist(tag);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_INSERTED, e);
    }
  }

  public Tag updateTag(Tag tag) {
    try {
      return tagDao.update(tag);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_UPDATED, e);
    }
  }

  public boolean deleteTag(Tag tagToDelete) {
    try {
      tagDao.delete(tagToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public boolean deleteTag(long id) {
    try {
      Tag tag = get(id);
      deleteTag(tag);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public Tag get(Tag tag) {
    try {
      return get(tag.getId());
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Tag get(Long id) {
    Tag tag = tagDao.get(id);
    if (tag == null) {
      throw new SystemException(UserError.USER_NOT_FOUND);
    }
    return tag;
  }

  public List<Tag> getAll() {
    try {
      return tagDao.getAll();
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Tag transformToTag(Request request) {
    Tag tag = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      tag = get(id);
    } else {
      tag = new Tag();
    }

    if (request.queryParams("name") != null) {
      tag.setName(request.queryParams("name"));
    }

    LOGGER.info("Received: " + tag.toString());

    return tag;
  }


}
