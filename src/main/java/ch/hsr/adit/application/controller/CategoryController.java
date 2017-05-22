package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.log4j.Logger;

import ch.hsr.adit.application.service.CategoryService;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.domain.exception.ForbiddenException;
import ch.hsr.adit.domain.model.Category;
import spark.Request;

public class CategoryController {
  private static final Logger LOGGER = Logger.getLogger(CategoryController.class);

  private static final String CATEGORY_ROUTE = "/category";
  private static final String CATEGORY_BY_ID_ROUTE = "/category/:id";
  private static final String CATEGORIES_FILTERED_ROUTE = "/categories/";

  private final PermissionService permissionService;

  /**
   * API Controller for /user requests.
   * 
   * @param categoryService service class
   */
  public CategoryController(CategoryService categoryService, PermissionService permissionService) {
    this.permissionService = permissionService;

    // create
    post(CATEGORY_ROUTE, (request, response) -> {
      checkEditCategoriesPermission(getToken(request));

      Category category = categoryService.transformToCategory(request);
      return categoryService.createCategory(category);
    }, jsonTransformer());

    // read
    get(CATEGORY_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return categoryService.get(id);
    }, jsonTransformer());

    get(CATEGORIES_FILTERED_ROUTE, (request, response) -> categoryService.getAllFiltered(request),
        jsonTransformer());

    // update
    put(CATEGORY_BY_ID_ROUTE, (request, response) -> {
      Category category = categoryService.transformToCategory(request);
      long id = Long.parseLong(request.params(":id"));
      category.setId(id);

      // check if category exists
      categoryService.get(category);
      checkEditCategoriesPermission(getToken(request));

      return categoryService.updateCategory(category);
    }, jsonTransformer());

    // delete
    delete(CATEGORY_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));

      // check if category exists
      categoryService.get(id);
      checkEditCategoriesPermission(getToken(request));

      return categoryService.deleteCategory(id);
    }, jsonTransformer());
  }

  private void checkEditCategoriesPermission(String token) {
    if (!permissionService.checkEditCategoriesPermission(token)) {
      LOGGER.warn("User does not have permission to edit categories!");
      throw new ForbiddenException("User does not have permission to edit categories!");
    }
  }

  private String getToken(Request request) {
    return request.headers("Authorization");
  }
}
