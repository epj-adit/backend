package ch.hsr.adit.application.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Message;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.MessageDao;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class UserService {

  private static final Logger LOGGER = Logger.getLogger(UserService.class);
  private final UserDao userDao;
  private final MessageDao messageDao;

  public UserService(UserDao userDao, MessageDao messageDao) {
    this.userDao = userDao;
    this.messageDao = messageDao;
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
      
      String hashed = BCrypt.hashpw(user.getPasswordPlaintext(), BCrypt.gensalt());
      if (! hashed.equals(dbUser.getPasswordHash())) {
        dbUser.setPasswordHash(hashed);
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

    } catch (HibernateException e) {
      LOGGER.warn("User with id " + user.getId() + " not found. Nothing updated");
      return user;
    }
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
  
  public User getByEmail(String email) {
    return userDao.getUserByEmail(email);
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

  public List<User> getAllFiltered(Request request) {
    if (request.queryParams("conversationUserId") != null) {
      Long conversationUserId = Long.parseLong(request.queryParams("conversationUserId"));
      return getByConversation(conversationUserId);
    }

    // TODO replace with other filter: need feedback!
    return null;
  }

  private List<User> getByConversation(Long conversationUserId) {
    List<Message> messages = messageDao.getByConversation(conversationUserId);

    // we use a Set to suppress duplicates
    Set<User> conversationUsers = new HashSet<>();
    for (Message message : messages) {
      conversationUsers.add(message.getUserByRecipientUserId());
      conversationUsers.add(message.getUserBySenderUserId());
    }
    return Arrays.asList(conversationUsers.toArray(new User[conversationUsers.size()]));
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