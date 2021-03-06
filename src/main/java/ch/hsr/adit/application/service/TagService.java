package ch.hsr.adit.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.AdvertisementState;
import ch.hsr.adit.domain.model.Tag;
import ch.hsr.adit.domain.model.filter.AdvertisementFilter;
import ch.hsr.adit.domain.persistence.AdvertisementDao;
import ch.hsr.adit.domain.persistence.TagDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class TagService {

  private static final Logger LOGGER = Logger.getLogger(TagService.class);

  private final TagDao tagDao;
  private final AdvertisementDao advertisementDao;

  public TagService(TagDao tagDao, AdvertisementDao advertisementDao) {
    this.tagDao = tagDao;
    this.advertisementDao = advertisementDao;
  }

  public List<Tag> createTags(List<Tag> tags) {
    List<Tag> persisted = new ArrayList<>();
    for (Tag tag : tags) {
      if (tag.getName() != null && !tag.getName().isEmpty()) {
        Tag dbTag = tagDao.getByName(tag.getName());
        if (dbTag == null) {
          dbTag = tagDao.persist(tag);
        }
        persisted.add(dbTag);
      } else {
        throw new PropertyValueException("Tagname cannot be null or empty!", "Tag", "Name");
      }
    }
    return persisted;
  }

  public boolean deleteTag(Tag tagToDelete) {
    if (tagToDelete != null) {
      List<Advertisement> relatedAdvertisements = advertisementDao.getByTag(tagToDelete.getName());
      if (relatedAdvertisements == null || relatedAdvertisements.isEmpty()) {
        tagDao.delete(tagToDelete);
      }
    }
    return true;
  }

  public boolean deleteTag(long id) {
    Tag tag = get(id);
    return deleteTag(tag);
  }

  public Tag get(Long id) {
    return tagDao.get(id);
  }

  public List<Tag> getAllFiltered(Request request) {
    final String name = request.queryParams("name");
    if (name != null && !name.isEmpty()) {
      return getActiveTags().stream()
          .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  public List<Tag> getActiveTags() {
    AdvertisementFilter filter = new AdvertisementFilter();
    filter.setAdvertisementStates(Arrays.asList(AdvertisementState.ACTIVE));
    final List<Advertisement> advertisements = advertisementDao.get(filter);

    List<Tag> activeTags = new ArrayList<>();
    advertisements.forEach(a -> activeTags.addAll(a.getTags()));

    return activeTags;
  }

  public List<Tag> transformToTags(Request request) {
    try {
      List<Tag> tag = JsonUtil.fromJson(request.body(), new TypeToken<List<Tag>>() {}.getType());
      LOGGER.info("Received JSON data: " + tag.toString());
      return tag;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
