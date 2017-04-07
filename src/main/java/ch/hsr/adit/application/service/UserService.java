package ch.hsr.adit.application.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.exception.UserError;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.UserDao;
import spark.Request;


public class UserService {

  private static final Logger LOGGER = Logger.getLogger(UserService.class);
  private final UserDao userDao;
  private final RoleService roleService;


  public UserService(UserDao userDao, RoleService roleService) {
    this.userDao = userDao;
    this.roleService = roleService;
  }

  public User createUser(User user) {
    try {
      return (User) userDao.persist(user);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_INSERTED, e);
    }
  }

  public User updateUser(User user) {
    try {
      return userDao.update(user);
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_UPDATED, e);
    }
  }

  public boolean deleteUser(User userToDelete) {
    try {
      userDao.delete(userToDelete);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public boolean deleteUser(long id) {
    try {
      User user = get(id);
      deleteUser(user);
      return true;
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_DELETED, e);
    }
  }

  public User get(User user) {
    try {
      return get(user.getId());
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public User get(Long id) {
    User user = userDao.get(id);
    if (user == null) {
      throw new SystemException(UserError.USER_NOT_FOUND);
    }
    return user;
  }

  public List<User> getAll() {
    try {
      return userDao.getAll();
    } catch (Exception e) {
      throw new SystemException(UserError.USER_NOT_FOUND, e);
    }
  }

  public User transformToUser(Request request) {
    User user = null;
    if (request.params(":id") != null) {
      Long id = Long.parseLong(request.params(":id"));
      user = get(id);
    } else if (request.queryParams("id") != null) {
      Long id = Long.parseLong(request.queryParams("id"));
      user = get(id);
    } else {
      user = new User();
    }
    
    user.setUpdated(new Date());

    if (request.queryParams("username") != null) {
      user.setUsername(request.queryParams("username"));
    }

    if (request.queryParams("email") != null) {
      user.setEmail(request.queryParams("email"));
    }

    if (request.queryParams("passwordHash") != null) {
      user.setPasswordHash(request.queryParams("passwordHash"));
    }

    if (request.queryParams("isPrivate") != null) {
      user.setIsPrivate(Boolean.parseBoolean(request.queryParams("isPrivate")));
    }

    if (request.queryParams("wantsNotification") != null) {
      user.setWantsNotification(Boolean.parseBoolean(request.queryParams("wantsNotification")));
    }

    if (request.queryParams("isActive") != null) {
      user.setIsActive(Boolean.parseBoolean(request.queryParams("isActive")));
    }

    if (request.queryParams("jwtToken") != null) {
      user.setJwtToken(request.queryParams("jwtToken"));
    }

    if (request.queryParams("role") != null) {
      Role role = roleService.getRole(Long.parseLong(request.queryParams("role")));
      user.setRole(role);
    }
    
    

    // TODO add date util
    // userLogs
    // messagesForSenderUserId
    // messagesForRecipientUserId
    // subscriptions
    // advertisements

    LOGGER.info("Received: " + user.toString());

    return user;
  }


}
