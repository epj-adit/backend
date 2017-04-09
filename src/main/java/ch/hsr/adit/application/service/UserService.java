package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.exception.EntityError;
import ch.hsr.adit.domain.exception.SystemError;
import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class UserService {

  private static final Logger LOGGER = Logger.getLogger(UserService.class);
  private final UserDao userDao;


  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User createUser(User user) {
    try {
      return (User) userDao.persist(user);
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_INSERTED, e);
    }
  }

  public User updateUser(User user) {
    try {
      return userDao.update(user);
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_UPDATED, e);
    }
  }

  public boolean deleteUser(User userToDelete) {
    try {
      userDao.delete(userToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_DELETED, e);
    }
  }

  public boolean deleteUser(long id) {
    try {
      User user = get(id);
      deleteUser(user);
      return true;
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_DELETED, e);
    }
  }

  public User get(User user) {
    try {
      return get(user.getId());
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_FOUND, e);
    }
  }

  public User get(Long id) {
    User user = userDao.get(id);
    if (user == null) {
      throw new SystemException(EntityError.ENTITY_NOT_FOUND);
    }
    return user;
  }

  public List<User> getAll() {
    try {
      return userDao.getAll();
    } catch (Exception e) {
      throw new SystemException(EntityError.ENTITY_NOT_FOUND, e);
    }
  }
  
  public User transformToUser(Request request) {
    try {
      User user = JsonUtil.fromJson(request.body(), User.class);
      LOGGER.info("Received JSON data: " + user.toString());
      return user;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw new SystemException(SystemError.JSON_PARSE_ERROR);
    }
  }

}
