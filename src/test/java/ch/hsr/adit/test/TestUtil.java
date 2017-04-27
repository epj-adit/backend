package ch.hsr.adit.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import spark.route.HttpMethod;
import spark.utils.IOUtils;

public class TestUtil {

  private static final Logger LOGGER = Logger.getLogger(TestUtil.class);

  private static final String ENDPOINT = "http://localhost:4567";

  public static TestResponse request(HttpMethod method, String path, Object entity) {
    try {
      // setup connection
      URL url = new URL(ENDPOINT + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method.toString().toUpperCase());
      connection.setDoOutput(true);

      // post data
      if (entity != null) {
        String json = new Gson().toJson(entity);
        connection.setRequestProperty("Content-Type", "application/json");
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

}
