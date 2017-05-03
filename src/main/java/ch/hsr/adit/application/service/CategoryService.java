package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Category;
import ch.hsr.adit.domain.persistence.CategoryDao;
import ch.hsr.adit.util.JsonUtil;
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
    try {
      // may throws ObjectNotFoundException
      categoryDao.get(category.getId());
     
      return categoryDao.update(category);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Category with id " + category.getId() + " not found. Nothing updated");
      throw e;
    } 
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

  public List<Category> getAllFiltered(Request request) {
    String name = request.queryParams("name");
    if (name != null && !name.isEmpty()) {
      return categoryDao.getFiltered(name);
    } else {
      return categoryDao.getAll();
    }
  }


  public List<Category> getAll() {
    return categoryDao.getAll();
  }

  public Category transformToCategory(Request request) {
    try {
      Category category = JsonUtil.fromJson(request.body(), Category.class);
      LOGGER.info("Received JSON data: " + category.toString());
      return category;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }


}
