package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.persistence.CategoryDao;
import spark.Request;


public class CategoryService {

  private static final Logger LOGGER = Logger.getLogger(CategoryService.class);
  private final CategoryDao categoryDao;


  public CategoryService(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  public Category createCategory(Category category) {
    return (Category) categoryDao.persist(category);
  }

  public Category updateCategory(Category category) {
    return categoryDao.update(category);
  }

  public boolean deleteCategory(Category categoryToDelete) {
    categoryDao.delete(categoryToDelete);
    return true;
  }

  public boolean deleteCategory(long id) {
    Category category = get(id);
    deleteCategory(category);
    return true;
  }

  public Category get(Category category) {
    return get(category.getId());
  }

  public Category get(Long id) {
    Category category = categoryDao.get(id);
    return category;
  }

  public List<Category> getAll() {
    return categoryDao.getAll();
  }

  public Category transformToUser(Request request) {
    Category category = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      category = get(id);
    } else {
      category = new Category();
    }

    if (request.queryParams("name") != null) {
      category.setName(request.queryParams("name"));
    }

    if (request.queryParams("parentCategoryId") != null) {
      Long parentId = Long.parseLong(request.queryParams("parentCategoryId"));
      Category parentCategory = categoryDao.get(parentId);
      category.setParentCategory(parentCategory);
    }

    LOGGER.info("Received: " + category.toString());

    return category;
  }


}
