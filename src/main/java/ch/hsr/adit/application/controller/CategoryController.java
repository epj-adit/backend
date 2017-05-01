package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.app.RestApi;
import ch.hsr.adit.application.service.CategoryService;
import ch.hsr.adit.domain.model.Category;

public class CategoryController {

  /**
   * API Controller for /user requests.
   * 
   * @param categoryService service class
   */
  public CategoryController(CategoryService categoryService) {

    // create
    post(RestApi.Category.CATEGORY, (request, response) -> {
      Category category = categoryService.transformToCategory(request);
      return categoryService.createCategory(category);
    }, jsonTransformer());

    // read
    get(RestApi.Category.CATEGORY_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return categoryService.get(id);
    }, jsonTransformer());

    get(RestApi.Category.CATEGORIES_FILTERED, (request, response) -> {
      return categoryService.getAllFiltered(request);
    }, jsonTransformer());
    
    // update
    put(RestApi.Category.CATEGORY_BY_ID, (request, response) -> {
      Category category = categoryService.transformToCategory(request);
      long id = Long.parseLong(request.params(":id"));
      category.setId(id);
      return categoryService.updateCategory(category);
    }, jsonTransformer());

    // delete
    delete(RestApi.Category.CATEGORY_BY_ID, (request, response) -> {
      // TODO check for permisisons
      long id = Long.parseLong(request.params(":id"));
      return categoryService.deleteCategory(id);
    }, jsonTransformer());
  }
}
