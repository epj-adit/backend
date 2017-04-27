package ch.hsr.adit.application.service;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.JsonUtil;
import ch.hsr.adit.util.TokenUtil;
import spark.Request;
import spark.Spark;


public class AuthenticationService {

  private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class);

  public UserDao userDao;

  public AuthenticationService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User authenticate(Request request) {
    User user = parseUser(request);
    if (checkPassword(user)) {
      // set jwt token if password is valid
      String token = TokenUtil.getInstance().generateToken(user.getEmail());
      user.setJwtToken(token);
      return user;
    } else {
      LOGGER.warn("Wrong credentials given. No token created.");
      throw Spark.halt();
    }
  }

  private User parseUser(Request request) {
    try {
      JsonObject object = JsonUtil.simpleObject(request.body());
      String email = object.get("email").getAsString();
      User user = userDao.getUserByEmail(email);

      String passwordPlaintext = object.get("passwordPlaintext").getAsString();
      user.setPasswordPlaintext(passwordPlaintext);
      return user;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse credentails. Invalid json received: " + e.getMessage());
      throw e;
    }
  }

  private boolean checkPassword(User user) {
    if (user == null || user.getPasswordHash() == null || user.getPasswordPlaintext() == null) {
      return false;
    }
    return BCrypt.checkpw(user.getPasswordPlaintext(), user.getPasswordHash());
  }

}
