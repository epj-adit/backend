package ch.hsr.adit.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;


public final class TokenUtil {

  // TODO write token to DB?
  // TODO: claims (rollen / rechte)

  private static final Logger LOGGER = Logger.getLogger(TokenUtil.class);

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
            LOGGER.error("Cannot load keystore: " + e.getMessage());
          }
        }
      }
    }
    return instance;
  }

  public String generateToken(String subject) {
    return JWT.create().withIssuer(ISSUER).withSubject(subject).sign(algorithm);
  }

  public Map<JwtClaim, Object> getClaims(String token) {
    try {
      JWT jwt = JWT.decode(token);

      Map<JwtClaim, Object> claims = new HashMap<>();
      claims.put(JwtClaim.SUBJECT, jwt.getSubject());

      return claims;
    } catch (JWTDecodeException e) {
      LOGGER.error("JWT token decoding failed: " + e.getMessage());
      throw e;
    }
  }

  public boolean verify(String token) {
    if (token == null || token.isEmpty()) {
      throw new IllegalArgumentException("Token must be provided");
    }
    
    String subject = (String) getClaims(token).get(JwtClaim.SUBJECT);
    
    JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).withSubject(subject).build();

    try {
      verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

}
