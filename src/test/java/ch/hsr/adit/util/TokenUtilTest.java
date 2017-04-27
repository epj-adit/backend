package ch.hsr.adit.util;

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
    String generatedToken = TokenUtil.getInstance().generateToken("student@hsr.ch");
    assertTrue(TokenUtil.getInstance().verify(generatedToken));
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullArgumentTest() {
    TokenUtil tokenUtil = TokenUtil.getInstance();
    tokenUtil.verify(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyArgumentTest() {
    TokenUtil tokenUtil = TokenUtil.getInstance();
    tokenUtil.verify("");
  }
}
