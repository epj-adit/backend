package ch.hsr.adit.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.util.TokenUtil;
import spark.route.HttpMethod;
import spark.utils.IOUtils;

public class TestUtil {

  private static final Logger LOGGER = Logger.getLogger(TestUtil.class);

  private static final String ENDPOINT = "http://localhost:4567";
  public static String testToken = null;
  public static boolean useToken = true;

  public static TestResponse request(HttpMethod method, String path, Object entity) {
    if (useToken && testToken == null) {
      createToken();
    }

    try {
      // setup connection
      URL url = new URL(ENDPOINT + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method.toString().toUpperCase());
      connection.setDoOutput(true);
      if (useToken) {
        connection.setRequestProperty("Authorization", testToken);
      }

      // post data
      if (entity != null) {
        String json = new Gson().toJson(entity);
        connection.addRequestProperty("Content-Type", "application/json");
        connection.getOutputStream().write(json.getBytes());
      }

      if (connection.getResponseCode() >= 400) {
        return new TestResponse(connection.getResponseCode(), null);
      }

      // retrieve response
      String body = IOUtils.toString(connection.getInputStream());
      return new TestResponse(connection.getResponseCode(), body);
    } catch (IOException ex) {
      LOGGER.error(ex.getMessage());
      return null;
    }
  }

  private static void createToken() {
    User user = createTestUser();
    testToken = TokenUtil.getInstance().generateToken(user);
  }
  
  private static User createTestUser() {
    User user = new User();
    user.setEmail("test@adit.ch");
    
    Role role = new Role();
    Permission permission = new Permission();
    permission.setName("test_permission");
    Set<Permission> permissions = new  HashSet<>();
    permissions.add(permission);
    role.setPermissions(permissions);
    
    user.setRole(role);
    return user;
  }

  public static void setUseToken(boolean use) {
    useToken = use;
  }

}
