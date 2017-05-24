package ch.hsr.adit.application.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

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
    if (user.getPasswordPlaintext() != null && !user.getPasswordPlaintext().isEmpty()) {
      // hash password with OpenBSD's Blowfish password hashing code
      String hashed = BCrypt.hashpw(user.getPasswordPlaintext(), BCrypt.gensalt());
      user.setPasswordHash(hashed);
    } else {
      user.setPasswordHash(null);
    }

    return (User) userDao.persist(user);
  }

  /**
   * Updates the user in database
   * 
   * Updates all fields including the updateDate except JwtToken, because the token only is set by
   * TokenUtil
   * 
   * @param user with new values
   * @return updated user
   */
  public User updateUser(User user) {
    try {
      User dbUser = userDao.get(user.getId());
      if (!user.getEmail().equals(dbUser.getEmail())) {
        dbUser.setEmail(user.getEmail());
      }

      if (user.getPasswordPlaintext() != null && !user.getPasswordPlaintext().isEmpty()) {
        String hashed = BCrypt.hashpw(user.getPasswordPlaintext(), BCrypt.gensalt());
        if (!hashed.equals(dbUser.getPasswordHash())) {
          dbUser.setPasswordHash(hashed);
        }
      }

      if (!user.getJwtToken().equals(dbUser.getJwtToken())) {
        dbUser.setJwtToken(user.getJwtToken());
      }

      if (!user.getRole().equals(dbUser.getRole())) {
        dbUser.setRole(user.getRole());
      }

      if (!user.getUsername().equals(dbUser.getUsername())) {
        dbUser.setUsername(user.getUsername());
      }

      if (user.isIsActive() != dbUser.isIsActive()) {
        dbUser.setIsActive(user.isIsActive());
      }

      if (user.isIsPrivate() != dbUser.isIsPrivate()) {
        dbUser.setIsPrivate(user.isIsPrivate());
      }

      if (user.isWantsNotification() != dbUser.isWantsNotification()) {
        dbUser.setWantsNotification(user.isWantsNotification());
      }
      dbUser.setUpdated(new Date());
      return userDao.update(dbUser);

    } catch (ObjectNotFoundException e) {
      LOGGER.warn("User with id " + user.getId() + " not found. Nothing updated");
      throw e;
    } catch (NullPointerException e) {
      LOGGER.warn("Nullpointer on user with id" + user.getId() + ". Nothing updated.");
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public User deleteUser(long id) {
    User user = get(id);
    user.setIsActive(false);
    userDao.update(user);
    return user;
  }

  public User getByEmail(String email) {
    return userDao.getUserByEmail(email);
  }

  public User get(User user) {
    return get(user.getId());
  }

  public User get(Long id) {
    return userDao.get(id);
  }

  public List<User> getAll() {
    return userDao.getAll();
  }

  public User transformToUser(Request request) {
    try {
      User user = JsonUtil.fromJson(request.body(), User.class);

      if (user.getJwtToken() == null || user.getJwtToken().isEmpty()) {
        String token = request.headers("Authorization");
        if (token != null && !token.isEmpty()) {
          user.setJwtToken(token);
        } else {
          LOGGER.warn("User transformed, but no token provided.");
        }
      }

      LOGGER.info("Received JSON data: " + user.toString());
      return user;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
