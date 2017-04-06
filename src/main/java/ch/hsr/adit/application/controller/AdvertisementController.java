package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.AdvertisementService;
import ch.hsr.adit.domain.model.Advertisement;

public class AdvertisementController {

  /**
   * API Controller for /advertisement requests.
   * 
   * @param AdvertisementService class
   */
  public AdvertisementController(AdvertisementService advertisementService) {

    // create
    post(RestApi.Advertisement.ADVERTISEMENT, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      return advertisementService.createAdvertisement(advertisement);
    }, jsonTransformer());

    // read
    get(RestApi.Advertisement.ADVERTISEMENT_BY_ID, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      return advertisementService.get(advertisement);
    }, jsonTransformer());

    get(RestApi.Advertisement.ADVERTISEMENTS, (request, response) -> {
      return advertisementService.getAll();
    }, jsonTransformer());

    // update
    put(RestApi.Advertisement.ADVERTISEMENT_BY_ID, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      return advertisementService.updateAdvertisement(advertisement);
    }, jsonTransformer());

    // delete
    delete(RestApi.Advertisement.ADVERTISEMENT_BY_ID, (request, response) -> {
      Advertisement advertisement = advertisementService.transformToAdvertisement(request);
      return advertisementService.deleteAdvertisement(advertisement);
    }, jsonTransformer());
  }
}
