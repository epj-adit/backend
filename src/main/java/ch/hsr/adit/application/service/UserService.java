package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

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
    return (User) userDao.persist(user);
  }

  public User updateUser(User user) {
    return userDao.update(user);
  }

  public boolean deleteUser(User userToDelete) {
    userDao.delete(userToDelete);
    return true;
  }

  public boolean deleteUser(long id) {
    User user = get(id);
    deleteUser(user);
    return true;
  }

  public User get(User user) {
    return get(user.getId());
  }
  
  public User get(Long id) {
    User user = userDao.get(id);
    return user;
  }

  public List<User> getAll() {
    return userDao.getAll();
  }

  public User transformToUser(Request request) {
    try {
      User user = JsonUtil.fromJson(request.body(), User.class);
      LOGGER.info("Received JSON data: " + user.toString());
      return user;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
