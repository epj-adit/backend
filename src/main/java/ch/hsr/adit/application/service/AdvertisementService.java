package ch.hsr.adit.application.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.AdvertisementState;
import ch.hsr.adit.domain.model.filter.AdvertisementFilter;
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
    return advertisementDao.persist(advertisement);
  }

  public Advertisement updateAdvertisement(Advertisement advertisement) {
    try {
      // may throw ObjectNotFoundException
      advertisementDao.get(advertisement.getId());

      advertisement.setUpdated(new Date());
      return advertisementDao.update(advertisement);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Advertisement with id " + advertisement.getId() + " not found. Nothing updated");
      throw e;
    }
  }

  public Advertisement deleteAdvertisement(Advertisement advertisement) {
    advertisement.setAdvertisementState(AdvertisementState.CLOSED);
    advertisementDao.update(advertisement);
    return advertisement;
  }

  public Advertisement get(Long id) {
    return advertisementDao.get(id);
  }

  public List<Advertisement> getAllFiltered(Request request) {

    AdvertisementFilter filter = new AdvertisementFilter();

    if (request.queryParams("userId") != null) {
      Long userId = Long.parseLong(request.queryParams("userId"));
      filter.setUserId(userId);
    }

    if (request.queryParamsValues("advertisementState") != null) {
      String[] tags = request.queryParamsValues("advertisementState");
      for (int i = 0; i < tags.length; i++) {
        Integer ordinal = Integer.parseInt(tags[i]);
        filter.getAdvertisementStates().add(AdvertisementState.values()[ordinal]);
      }
    }

    if (request.queryParamsValues("tagId") != null) {
      String[] tags = request.queryParamsValues("tagId");
      for (int i = 0; i < tags.length; i++) {
        filter.getTagIds().add(Long.valueOf(tags[i]));
      }
    }

    if (request.queryParams("categoryId") != null) {
      String[] categories = request.queryParamsValues("categoryId");
      for (int i = 0; i < categories.length; i++) {
        filter.getCategoryIds().add(Long.valueOf(categories[i]));
      }
    }

    filter.setTitle(request.queryParams("title"));
    filter.setDescription(request.queryParams("description"));


    if (filter.isEmpty()) {
      return advertisementDao.getAll();
    } else {
      return advertisementDao.get(filter);
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
