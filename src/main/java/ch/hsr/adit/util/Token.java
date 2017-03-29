package ch.hsr.adit.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import ch.hsr.adit.domain.model.User;
import lombok.Data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


@Data
public final class Token {

  // write token to DB?
  // TODO: claims (rollen / rechte)
  private Algorithm algorithm;
  private JWTVerifier verifier;
  private DecodedJWT jwt;
  public String token; //enthält das jwt base 64 codiert
  private SecretKey secret;
  private KeyStore keyStore;
  /**
   * Constructor to create a TokenUtil that contains the Users token
   * @param user
   */
  public Token(User user) {
    try {
      keyStore = KeyStore.getInstance();
      secret = KeyStore.loadKey();
      algorithm = Algorithm.HMAC256(secret.getEncoded());
      token = JWT.create().withIssuer("adit").withSubject(user.getEmail()).sign(algorithm);
    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    user.setJwtToken(token);
    verifier = JWT.require(algorithm).withIssuer("adit").withSubject(user.getEmail()).build();
  }

  /*
   * verifies the given token 
   * throws JWTVerificationException if token has an invalid signature
   * @param token to identify the user
   */
  public void verify(User user) {
    jwt = verifier.verify(user.getJwtToken());
  }
}
