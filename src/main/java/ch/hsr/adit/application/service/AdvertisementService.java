package ch.hsr.adit.application.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.exception.UserError;
import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.persistence.AdvertisementDao;
import spark.Request;


public class AdvertisementService {

  private static final Logger LOGGER = Logger.getLogger(AdvertisementService.class);
  private final AdvertisementDao advertisementDao;
  private final CategoryService categoryService;
  private final TagService tagService;
  private final UserService userService;

  public AdvertisementService(AdvertisementDao advertisementDao, UserService userService,
      TagService tagService, CategoryService categoryService) {
    
    this.advertisementDao = advertisementDao;
    this.userService = userService;
    this.tagService = tagService;
    this.categoryService = categoryService;
  }

  public Advertisement createAdvertisement(Advertisement advertisement) {
    try {
      return (Advertisement) advertisementDao.persist(advertisement);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_INSERTED, e);
    }
  }

  public Advertisement updateAdvertisement(Advertisement advertisement) {
    try {
      return advertisementDao.update(advertisement);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_UPDATED, e);
    }
  }

  public boolean deleteAdvertisement(Advertisement advertisementToDelete) {
    try {
      advertisementDao.delete(advertisementToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public boolean deleteAdvertisement(long id) {
    try {
      Advertisement advertisement = get(id);
      deleteAdvertisement(advertisement);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public Advertisement get(Advertisement advertisement) {
    try {
      return get(advertisement.getId());
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Advertisement get(Long id) {
    Advertisement advertisement = advertisementDao.get(id);
    if (advertisement == null) {
      throw new SystemException(UserError.USER_NOT_FOUND);
    }
    return advertisement;
  }

  public List<Advertisement> getAll() {
    try {
      return advertisementDao.getAll();
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Advertisement transformToAdvertisement(Request request) {
    Advertisement advertisement = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      advertisement = get(id);
    } else {
      advertisement = new Advertisement();
    }

    if (request.queryParams("title") != null) {
      advertisement.setTitle(request.queryParams("title"));
    }

    if (request.queryParams("description") != null) {
      advertisement.setDescription(request.queryParams("description"));
    }

    if (request.queryParams("price") != null) {
      advertisement.setPrice(Integer.parseInt(request.queryParams("price")));
    }

    if (request.queryParams("updated") != null) {
      // TODO parse updated date. Take it from client or set new date?
      advertisement.setUpdated(new Date());
    }

    if (request.queryParams("categoryId") != null) {
      Category category = categoryService.get(Long.parseLong(request.queryParams("categoryId")));
      advertisement.setCategory(category);
    }

    LOGGER.info("Received: " + advertisement.toString());

    return advertisement;
  }


}
