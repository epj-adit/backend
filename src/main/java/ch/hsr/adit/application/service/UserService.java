package ch.hsr.adit.application.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

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

  public User getByEmail(Request request) {
    String email = request.queryParams("email");
    if (email != null && !email.isEmpty()) {
      return userDao.getUserByEmail(email);
    } else {
      LOGGER.error("Failed to fetch user by email. No email given.");
      throw new IllegalArgumentException("Failed to fetch user by email. No email given.");
    }
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
    String email = request.queryParams("email");
    return userDao.get(email);
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
