package ch.hsr.adit.application.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.AdvertisementState;
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
    return (Advertisement) advertisementDao.persist(advertisement);
  }

  public Advertisement updateAdvertisement(Advertisement advertisement) {
    return advertisementDao.update(advertisement);
  }

  public boolean deleteAdvertisement(Advertisement advertisementToDelete) {
    advertisementDao.delete(advertisementToDelete);
    return true;
  }

  public boolean deleteAdvertisement(long id) {
    Advertisement advertisement = get(id);
    return deleteAdvertisement(advertisement);
  }

  public Advertisement get(Long id) {
    Advertisement advertisement = advertisementDao.get(id);
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

    List<AdvertisementState> states = new ArrayList<>();
    if (request.queryParamsValues("advertisementState") != null) {
      String[] tags = request.queryParamsValues("advertisementState");
      for (int i = 0; i < tags.length; i++) {
        Integer ordinal = Integer.parseInt(tags[i]);
        states.add(AdvertisementState.values()[ordinal]);
      }
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
    return advertisementDao.get(title, description, userId, states, categoryIds, tagIds);
  }

  public Advertisement transformToAdvertisement(Request request) {
    try {
      Advertisement advertisement = JsonUtil.fromJson(request.body(), Advertisement.class);
      LOGGER.info("Received JSON data: " + advertisement.toString());
      return advertisement;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
