package ch.hsr.adit.util;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public final class TokenUtil {

  // TODO write token to DB?
  // TODO: claims (rollen / rechte)

  private static final Logger logger = Logger.getLogger(TokenUtil.class);

  private static final String ISSUER = "adit";

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
            logger.error("Cannot load keystore: " + e.getMessage());
          }
        }
      }
    }
    return instance;
  }

  public String generateToken(String subject) {
    return JWT.create().withIssuer(ISSUER).withSubject(subject).sign(algorithm);
  }

  public boolean verify(String token, String subject) {
    if (token == null || subject == null || token.isEmpty() || subject.isEmpty()) {
      throw new IllegalArgumentException("Token and subject must be set");
    }
    
    JWTVerifier verifier =
        JWT.require(algorithm).withIssuer(ISSUER).withSubject(subject).build();

    try {
      verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }
}
