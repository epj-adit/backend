package ch.hsr.adit.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

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
    try {
      // may throws ObjectNotFoundException
      advertisementDao.get(advertisement.getId());
      
      advertisement.setUpdated(new Date());
      return advertisementDao.update(advertisement);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Advertisement with id " + advertisement.getId() + " not found. Nothing updated");
      throw e;
    }
  }

  public Advertisement deleteAdvertisement(long id) {
    Advertisement advertisement = get(id);
    advertisement.setAdvertisementState(AdvertisementState.CLOSED);
    advertisementDao.update(advertisement);
    return advertisement;
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


    if (title == null && description == null && userId == null && states.isEmpty()
        && tagIds.isEmpty() && categoryIds.isEmpty()) {

      return advertisementDao.getAll();
    } else {
      return advertisementDao.get(title, description, userId, states, categoryIds, tagIds);
    }
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
