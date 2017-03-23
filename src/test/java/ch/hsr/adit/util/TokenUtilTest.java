package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;

import com.auth0.jwt.exceptions.JWTVerificationException;

import ch.hsr.adit.domain.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilTest {
  User user;
  TokenUtil token;

  @Before
  public void setUp() {
    user = new User();
    user.setEmail("student@hsr.ch");
  }

  @Test
  public void createTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    token = new TokenUtil(user);
    assertEquals(user.getToken(), token.getToken());
  }

  @Test
  public void verifyTokenTest() {
    token = new TokenUtil(user);
    token.verify(user);
  }

  @Test(expected = JWTVerificationException.class)
  public void verifyWrongTokenTest() {
    token = new TokenUtil(user);
    User user2 = new User();
    user2.setEmail("professor@hsr.ch");
    TokenUtil token2 = new TokenUtil(user2);
    token2.verify(user);
  }
}
