package ch.hsr.adit.application.service;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Credential;
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

  public User authenticate(Request request) throws AuthenticationException {
    Credential credentials = JsonUtil.fromJson(request.body(), Credential.class);
    String email = credentials.getEmail();
    User user = userDao.getUserByEmail(email);
    if (checkPassword(user.getPasswordHash(), credentials.getPlaintextPassword()) && user.isIsActive()) {
      // set jwt token if password is valid
      String token = TokenUtil.getInstance().generateToken(user);
      user.setJwtToken(token);
      return user;
    } else {

      if (!user.isIsActive()) {
        LOGGER.warn("User is not active! No token created.");
        throw new AuthenticationException("User is not active!");
      }
      LOGGER.warn("Wrong password given. No token created.");
      throw new AuthenticationException("Wrong password! No token created.");
    }
  }

  private boolean checkPassword(String passwordHash, String plaintextPassword) {
    if (passwordHash == null || plaintextPassword == null) {
      return false;
    }
    return BCrypt.checkpw(plaintextPassword, passwordHash);
  }

}
