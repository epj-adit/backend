package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.log4j.Logger;

import ch.hsr.adit.application.service.AdvertisementService;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.domain.exception.ForbiddenException;
import ch.hsr.adit.domain.model.Advertisement;
import spark.Request;

public class AdvertisementController {

  private static final Logger LOGGER = Logger.getLogger(AdvertisementController.class);
  
  private static final String ADVERTISEMENT_ROUTE = "/advertisement";
  private static final String ADVERTISEMENT_BY_ID_ROUTE = "/advertisement/:id";
  private static final String ADVERTISEMENTS_FILTERED_ROUTE = "/advertisements/";
  
  private final PermissionService permissionService;

  /**
   * API Controller for /advertisement requests.
   * 
   * @param AdvertisementService class
   */
  public AdvertisementController(AdvertisementService advertisementService,
      PermissionService permissionService) {
    this.permissionService = permissionService;

    // create
    post(ADVERTISEMENT_ROUTE, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      return advertisementService.createAdvertisement(advertisement);
    }, jsonTransformer());

    // read
    get(ADVERTISEMENT_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return advertisementService.get(id);
    }, jsonTransformer());

    get(ADVERTISEMENTS_FILTERED_ROUTE,
        (request, response) -> advertisementService.getAllFiltered(request), jsonTransformer());

    // update
    put(ADVERTISEMENT_BY_ID_ROUTE, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      long id = Long.parseLong(request.params(":id"));
      advertisement.setId(id);

      String token = getToken(request);
      checkBasicPermissions(advertisement, token);
      checkReviewAdvertisementPermission(token);

      return advertisementService.updateAdvertisement(advertisement);
    }, jsonTransformer());

    // delete
    delete(ADVERTISEMENT_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      Advertisement advertisement = advertisementService.get(id);

      checkBasicPermissions(advertisement, getToken(request));

      return advertisementService.deleteAdvertisement(advertisement);
    }, jsonTransformer());
  }

  private void checkBasicPermissions(Advertisement advertisement, String token) {
    if (!permissionService.checkBasicPermissions(advertisement.getUser(), token)) {
      LOGGER.warn("User does not have permission to access Advertisement with id "
          + advertisement.getId() + " !");
      throw new ForbiddenException("User does not have permission to access Advertisement with id "
          + advertisement.getId() + " !");
    }
  }

  private void checkReviewAdvertisementPermission(String token) {
    if (!permissionService.checkReviewAdvertisementPermission(token)) {
      LOGGER.warn("Additional permission is needed for editing other users status'!");
      throw new ForbiddenException(
          "Additional permission is needed for editing other users status'!");
    }
  }

  private String getToken(Request request) {
    return request.headers("Authorization");
  }
}
