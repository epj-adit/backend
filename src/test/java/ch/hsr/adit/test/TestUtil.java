package ch.hsr.adit.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

import spark.route.HttpMethod;
import spark.utils.IOUtils;

public class TestUtil {

  private static final Logger logger = Logger.getLogger(TestUtil.class);
  
  private static final String ENDPOINT = "http://localhost:4567";

  public static TestResponse request(HttpMethod method, String path, Map<String, Object> params) {
    try {
      // setup connection
      URL url = new URL(ENDPOINT + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method.toString().toUpperCase());
      connection.setDoOutput(true);

      // post data
      if (params != null ) {
        byte[] data = getParamData(params);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(data.length));
        connection.getOutputStream().write(data);
      }
      
      if (connection.getResponseCode() == 404) {
        return new TestResponse(connection.getResponseCode(), null);
      }

      // retrieve response
      String body = IOUtils.toString(connection.getInputStream());
      return new TestResponse(connection.getResponseCode(), body);
    } catch (IOException ex) {
      logger.error(ex.getMessage());
      return null;
    }
  }

  private static byte[] getParamData(Map<String, Object> params)
      throws UnsupportedEncodingException {
   
    StringBuilder postData = new StringBuilder();
    for (Map.Entry<String, Object> param : params.entrySet()) {
      if (postData.length() != 0) {
        postData.append('&');
      }
      postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
      postData.append('=');
      postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
    }
    return postData.toString().getBytes("UTF-8");
  }

}
