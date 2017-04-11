package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.TagService;
import ch.hsr.adit.domain.model.Tag;

public class TagController {

  /**
   * API Controller for /advertisement requests.
   * 
   * @param AdvertisementService class
   */
  public TagController(TagService tagService) {

    // create
    post(RestApi.Tag.TAG, (request, response) -> {
      Tag tag = tagService.transformToTag(request);
      return tagService.createTag(tag);
    }, jsonTransformer());

    // read
    get(RestApi.Tag.TAG_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return tagService.get(id);
    }, jsonTransformer());

    // update
    put(RestApi.Tag.TAG_BY_ID, (request, response) -> {
      Tag tag = tagService.transformToTag(request);
      return tagService.updateTag(tag);
    }, jsonTransformer());

    // delete
    delete(RestApi.Tag.TAG_BY_ID, (request, response) -> {
      // TODO check for permisisons
      long id = Long.parseLong(request.params(":id"));
      return tagService.deleteTag(id);
    }, jsonTransformer());
  }
}
