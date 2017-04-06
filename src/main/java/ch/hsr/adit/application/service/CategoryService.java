package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.exception.UserError;
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
    try {
      return (Category) categoryDao.persist(category);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_INSERTED, e);
    }
  }

  public Category updateCategory(Category category) {
    try {
      return categoryDao.update(category);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_UPDATED, e);
    }
  }

  public boolean deleteCategory(Category categoryToDelete) {
    try {
      categoryDao.delete(categoryToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public boolean deleteCategory(long id) {
    try {
      Category category = get(id);
      deleteCategory(category);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public Category get(Category category) {
    try {
      return get(category.getId());
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Category get(Long id) {
    Category category = categoryDao.get(id);
    if (category == null) {
      throw new SystemException(UserError.USER_NOT_FOUND);
    }
    return category;
  }

  public List<Category> getAll() {
    try {
      return categoryDao.getAll();
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public Category transformToUser(Request request) {
    Category category = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      category = get(id);
    } else {
      category = new Category();
    }


    LOGGER.info("Received: " + category.toString());

    return category;
  }


}
