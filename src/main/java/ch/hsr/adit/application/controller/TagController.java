package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;

import ch.hsr.adit.application.service.TagService;
import ch.hsr.adit.domain.model.Tag;

public class TagController {

  private static final String TAG_BY_ID_ROUTE = "/tag/:id";
  private static final String TAGS_ROUTE = "/tags/";
  private static final String TAGS_FILTERED_ROUTE = "/tags/";
  
  /**
   * API Controller for /advertisement requests.
   * 
   * @param AdvertisementService class
   */
  public TagController(TagService tagService) {

    // create
    post(TAGS_ROUTE, (request, response) -> {
      List<Tag> tags = tagService.transformToTags(request);
      return tagService.createTags(tags);
    }, jsonTransformer());

    // read
    get(TAG_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return tagService.get(id);
    }, jsonTransformer());

    get(TAGS_FILTERED_ROUTE, (request, response) -> tagService.getAllFiltered(request),
        jsonTransformer());

    // delete
    delete(TAG_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return tagService.deleteTag(id);
    }, jsonTransformer());
  }
}
