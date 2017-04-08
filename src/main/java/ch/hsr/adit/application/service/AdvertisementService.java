package ch.hsr.adit.application.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.exception.EntityError;
import ch.hsr.adit.domain.exception.SystemError;
import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.persistence.AdvertisementDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class AdvertisementService {

  private static final Logger LOGGER = Logger.getLogger(AdvertisementService.class);
  private final AdvertisementDao advertisementDao;

  public AdvertisementService(AdvertisementDao advertisementDao) {

    this.advertisementDao = advertisementDao;
  }

  public Advertisement createAdvertisement(Advertisement advertisement) {
    try {
      return (Advertisement) advertisementDao.persist(advertisement);
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_INSERTED, e);
    }
  }

  public Advertisement updateAdvertisement(Advertisement advertisement) {
    try {
      return advertisementDao.update(advertisement);
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_UPDATED, e);
    }
  }

  public boolean deleteAdvertisement(Advertisement advertisementToDelete) {
    try {
      advertisementDao.delete(advertisementToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_DELETED, e);
    }
  }

  public boolean deleteAdvertisement(long id) {
    try {
      Advertisement advertisement = get(id);
      deleteAdvertisement(advertisement);
      return true;
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_DELETED, e);
    }
  }

  public Advertisement get(Long id) {
    Advertisement advertisement = advertisementDao.get(id);
    if (advertisement == null) {
      throw new SystemException(EntityError.ENTITY_NOT_FOUND);
    }
    return advertisement;
  }

  public List<Advertisement> getAll() {
    return advertisementDao.getAll();
  }

  public List<Advertisement> getAllFiltered(Request request) {
    Long userId = null;
    if (request.queryParams("userId") != null) {
      userId = Long.parseLong(request.queryParams("userId"));
    }
    
    List<Long> tagIds = new ArrayList<>();
    if (request.queryParamsValues("tagId") != null) {
      String[] tags = request.queryParamsValues("tagId");
      for (int i = 0; i < tags.length; i++) {
        tagIds.add(Long.valueOf(tags[i]));
      }
    }

    List<Long> categoryIds = new ArrayList<>();
    if (request.queryParams("categoryId") != null) {
      String[] categories = request.queryParamsValues("categoryId");
      for (int i = 0; i < categories.length; i++) {
        categoryIds.add(Long.valueOf(categories[i]));
      }
    }

    String title = request.queryParams("title");
    String description = request.queryParams("description");
    return advertisementDao.get(title, description, userId, categoryIds, tagIds);
  }

  public Advertisement transformToAdvertisement(Request request) {
    try {
      Advertisement advertisement = JsonUtil.fromJson(request.body(), Advertisement.class);
      LOGGER.info("Received JSON data: " + advertisement.toString());
      return advertisement;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw new SystemException(SystemError.JSON_PARSE_ERROR);
    }
  }

}
