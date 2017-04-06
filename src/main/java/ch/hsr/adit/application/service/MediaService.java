package ch.hsr.adit.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.exception.UserError;
import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.Media;
import ch.hsr.adit.domain.persistence.MediaDao;
import spark.Request;


public class MediaService {

  private static final Logger LOGGER = Logger.getLogger(MediaService.class);

  private final MediaDao mediaDao;
  private final AdvertisementService advertisementService;

  public MediaService(MediaDao mediaDao, AdvertisementService advertisementService) {
    this.mediaDao = mediaDao;
    this.advertisementService = advertisementService;
  }

  public Media createMedia(Media media) {
    try {
      return (Media) mediaDao.persist(media);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_INSERTED, e);
    }
  }

  public Media updateMedia(Media media) {
    try {
      return mediaDao.update(media);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_UPDATED, e);
    }
  }

  public boolean deleteMedia(Media mediaToDelete) {
    try {
      mediaDao.delete(mediaToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public boolean deleteMedia(long id) {
    try {
      Media media = get(id);
      deleteMedia(media);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public Media get(Media media) {
    try {
      return get(media.getId());
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Media get(Long id) {
    Media media = mediaDao.get(id);
    if (media == null) {
      throw new SystemException(UserError.USER_NOT_FOUND);
    }
    return media;
  }

  public List<Media> getAll() {
    try {
      return mediaDao.getAll();
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Media transformToMedia(Request request) {
    Media media = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      media = get(id);
    } else {
      media = new Media();
    }

    if (request.queryParams("filename") != null) {
      media.setFilename(request.queryParams("filename"));
    }

    if (request.queryParams("descripton") != null) {
      media.setDescription(request.queryParams("description"));
    }

    if (request.queryParams("advertisementId") != null) {
      Advertisement advertisement =
          advertisementService.get(Long.parseLong(request.queryParams("advertisementId")));
      media.setAdvertisement(advertisement);
    }

    byte[] file = createFile(request);
    media.setMedia(file);

    LOGGER.info("Received: " + media.toString());

    return media;
  }

  private byte[] createFile(Request request) {
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
      return IOUtils.toByteArray(is);
    } catch (IOException | ServletException e) {
      LOGGER.error("Unable to create file");
      return null;
    }
  }

}
