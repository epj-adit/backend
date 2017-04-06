package ch.hsr.adit.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ch.hsr.adit.domain.model.User;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilTest {

  private static KeyStore keyStore;

  @BeforeClass
  public static void setUpClass() throws FileNotFoundException, NoSuchAlgorithmException {
    File file = new File("KeyStore.properties");
    keyStore = KeyStore.getInstance();
    keyStore.generateKey(file);
  }

  @Test
  public void createAndVerifyTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");

    TokenUtil tokenUtil = TokenUtil.getInstance();
    String token = tokenUtil.generateToken(user.getEmail());
    assertNotNull(token);
  }

  @Test
  public void verifyTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    TokenUtil tokenUtil = TokenUtil.getInstance();
    String generatedToken = tokenUtil.generateToken(user.getEmail());
    user.setJwtToken(generatedToken);

    assertTrue(tokenUtil.verify(user.getJwtToken(), user.getEmail()));
  }

  @Test
  public void verifyWrongTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    user.setJwtToken("spoofed");

    TokenUtil tokenUtil = TokenUtil.getInstance();
    assertFalse(tokenUtil.verify(user.getJwtToken(), user.getEmail()));
  }
  
  @Test
  public void verifyDifferentTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    User user2 = new User();
    user2.setEmail("professor@hsr.ch");
    TokenUtil tokenUtil = TokenUtil.getInstance();
    String token1 = tokenUtil.generateToken(user.getEmail());
    user.setJwtToken(token1);
    String token2 = tokenUtil.generateToken(user2.getEmail());
    user2.setJwtToken(token2);
    assertFalse(tokenUtil.verify(user.getJwtToken(), user2.getEmail()));
    assertFalse(tokenUtil.verify(token2, user.getEmail()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullArgumentTest() {
    TokenUtil tokenUtil = TokenUtil.getInstance();
    tokenUtil.verify(null, null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void emptyArgumentTest() {
    TokenUtil tokenUtil = TokenUtil.getInstance();
    tokenUtil.verify("", "");
  }
}
