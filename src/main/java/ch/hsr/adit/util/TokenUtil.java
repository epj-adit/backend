package ch.hsr.adit.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.domain.model.User;
import spark.Request;


public final class TokenUtil {

  private static final Logger LOGGER = Logger.getLogger(TokenUtil.class);

  private static final String ISSUER = "adit";
  private static int tokenTimeout = 24; // 24 hours
  private static int tokenLeeway = 120; // 2mins

  private static volatile TokenUtil instance;

  private static Algorithm algorithm;

  private TokenUtil() {}

  public static TokenUtil getInstance() {
    if (instance == null) {
      synchronized (KeyStore.class) {
        if (instance == null) {
          try {
            KeyStore keystore = KeyStore.getInstance();
            SecretKey secret = keystore.loadKey();
            TokenUtil.algorithm = Algorithm.HMAC256(secret.getEncoded());
            instance = new TokenUtil();
          } catch (IOException e) {
            LOGGER.error("Cannot load keystore");
            LOGGER.error(e);
          }
        }
      }
    }
    return instance;
  }

  public String generateToken(User user) {
    // get users permissions for dumb frontend people
    Set<Permission> permissionSet = user.getRole().getPermissions();
    Iterator<Permission> it = permissionSet.iterator();
    String[] permissions = new String[permissionSet.size()];
    for (int i = 0; i < permissions.length; i++) {
      Permission permission = it.next();
      permissions[i] = permission.getName();
    }

    // get expiration date
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR_OF_DAY, tokenTimeout);
    Date expirationDate = cal.getTime();
    return JWT.create().withIssuer(ISSUER).withSubject(user.getEmail())
        .withExpiresAt(expirationDate).withArrayClaim("permissions", permissions).sign(algorithm);
  }

  public boolean verify(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }
    JWT jwt = JWT.decode(token);
    String subject = jwt.getSubject();
    JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).withSubject(subject)
        .acceptLeeway(tokenLeeway).acceptExpiresAt(tokenTimeout).build();

    try {
      verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      LOGGER.error("Verification failed. Token is invalid");
      LOGGER.error(e);
      return false;
    }
  }

  public static void setTimeout(int timeout) {
    tokenTimeout = timeout;
  }

  public static void setLeeway(int leeway) {
    tokenLeeway = leeway;
  }

  public String getEmailFromToken(String token) {
    JWT jwt = JWT.decode(token);
    return jwt.getSubject();
  }
}
