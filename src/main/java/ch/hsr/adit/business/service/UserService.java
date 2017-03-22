package ch.hsr.adit.business.service;

import java.util.List;

import javax.persistence.OptimisticLockException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import ch.hsr.adit.exception.DatabaseException;
import ch.hsr.adit.model.User;
import ch.hsr.adit.persistence.GenericDao;
import spark.Request;


public class UserService {

  private static final Logger logger = Logger.getLogger(UserService.class);
  private final GenericDao<User, Long> userDao;

  public UserService(GenericDao<User, Long> userDao) {
    this.userDao = userDao;
  }

  public User createUser(User user) {
    try {
      return (User) userDao.persist(user);
    } catch (HibernateException e) {
      logger.error("HibernateException occured while creating user: " + e.getMessage());
      return null;
    }
  }

  /**
   * Update a user, if the given user exists in database. 
   * If user cannot be found, a OptimistcLockException is thrown. 
   * 
   * @param user to update with new values
   * @return same user
   */
  public User updateUser(User user) {
    try {
      return userDao.update(user);
    } catch (HibernateException | OptimisticLockException ex) {
      logger.error("Cannot update user. No changes made: " + ex.getMessage());
      throw new DatabaseException("cannot updated user");
    }

  }

  public boolean deleteUser(User userToDelete) {
    try {
      userDao.delete(userToDelete);
      return true;
    } catch (HibernateException e) {
      logger.error("HibernateException occured while deleting user: " + e.getMessage());
      return false;
    }
  }

  public boolean deleteUser(long id) {
    try {
      userDao.delete(id);
      return true;
    } catch (HibernateException e) {
      logger.error("HibernateException occured while deleting user: " + e.getMessage());
      return false;
    }
  }

  public User get(Long id) {
    try {
      return (User) userDao.get(id);
    } catch (HibernateException e) {
      logger.error("HibernateException occured while fetching user: " + e.getMessage());
      return null;
    }
  }

  public List<User> getAll() {
    try {
      return userDao.getAll();
    } catch (HibernateException e) {
      logger.error("HibernateException occured while creating user: " + e.getMessage());
      return null;
    }
  }

  public User transformToUser(Request request) {
    User user = new User();

    if (request.params(":id") != null) {
      user.setId(Long.parseLong(request.params(":id")));
    }

    user.setEmail(request.queryParams("email"));
    return user;
  }


}
