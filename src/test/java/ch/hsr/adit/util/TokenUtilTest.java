package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ch.hsr.adit.domain.model.User;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilTest {
  private User user;
  private Token token;
  private KeyStore keyStore;

  @Before
  public void setUp() throws FileNotFoundException, NoSuchAlgorithmException {
    File file = new File("KeyStore.properties");

    user = new User();
    user.setEmail("student@hsr.ch");
    keyStore = KeyStore.getInstance();
    keyStore.generateKey(file);
  }

  @Test
  public void createTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    token = new Token(user);
    assertEquals(user.getJwtToken(), token.getToken());
  }

  @Test
  public void verifyTokenTest() {
    token = new Token(user);
    token.verify(user);
  }

  @Test(expected = JWTVerificationException.class)
  public void verifyWrongTokenTest() {
    token = new Token(user);
    User user2 = new User();
    user2.setEmail("professor@hsr.ch");
    Token token2 = new Token(user2);
    token2.verify(user);
  }
}
