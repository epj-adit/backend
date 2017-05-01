package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;

import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilTest {

  private static KeyStore keyStore;
  private Role role;
  private Set<Permission> permissions;
  
  @BeforeClass
  public static void setUpClass() throws FileNotFoundException, NoSuchAlgorithmException {
    File file = new File("KeyStore.properties");
    keyStore = KeyStore.getInstance();
    keyStore.generateKey(file);
  }

  @Before
  public void setup() {
    this.permissions = new HashSet<Permission>();
    Permission perm = new Permission();
    perm.setId(1);
    perm.setName("review");
    Permission perm2 = new Permission();
    perm2.setId(2);
    perm2.setName("ban");
    permissions.add(perm);
    permissions.add(perm2);

    this.role = new Role();
    this.role.setId(1);
    this.role.setName("user");
    this.role.setPermissions(permissions);
  }

  @Test
  public void createAndVerifyTokenTest() {
    User user = new User();
    user.setEmail("student@hsr.ch");
    user.setRole(role);

    // set proper timeout, since earlier tests may have changed it
    TokenUtil.setTimeout(24);
    TokenUtil tokenUtil = TokenUtil.getInstance();
    String token = tokenUtil.generateToken(user);
    JWT jwt = JWT.decode(token);

    Map<String, Claim> claims = jwt.getClaims();
    String[] permissionArray = new String[permissions.size()];
    permissionArray = claims.get("permissions").asArray(String.class);

    assertNotNull(token);
    assertEquals("review", permissionArray[0]);
    assertEquals("ban", permissionArray[1]);
    assertTrue(TokenUtil.getInstance().verify(token));
  }

  @Test
  public void verifyTokenTest() {
    // set proper timeout, since earlier tests may have changed it
    TokenUtil.setTimeout(24);

    User user = new User();
    user.setEmail("student@hsr.ch");
    user.setRole(role);
    String generatedToken = TokenUtil.getInstance().generateToken(user);
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

  @Test(expected = JWTVerificationException.class)
  public void testVerificationWithWrongSecret() throws Exception {
    User user = new User();
    user.setEmail("student@hsr.ch");
    user.setRole(role);

    TokenUtil tokenUtil = TokenUtil.getInstance();

    String token = tokenUtil.generateToken(user);
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).withIssuer("adit")
        .withSubject("student@hsr.ch").acceptLeeway(1).acceptExpiresAt(1).build();
    verifier.verify(token);
  }

  @Test(expected = InvalidClaimException.class)
  public void testExpiredToken() throws Exception {
    User user = new User();
    user.setEmail("student@hsr.ch");
    user.setRole(role);

    TokenUtil tokenUtil = TokenUtil.getInstance();

    TokenUtil.setTimeout(-1);

    String token = tokenUtil.generateToken(user);

    SecretKey secret = keyStore.loadKey();
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret.getEncoded())).withIssuer("adit")
        .withSubject(user.getEmail()).acceptLeeway(0).acceptExpiresAt(1).build();
    JWT jwt = JWT.decode(token);
    verifier.verify(token);

    // reset tokenUtil
    TokenUtil.setTimeout(24);
  }
}
