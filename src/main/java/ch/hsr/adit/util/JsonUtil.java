package ch.hsr.adit.util;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonUtil {
  private static Gson gson = new Gson();

  /**
   * Converts the given object to json using Gson.
   * 
   * @param object to serialize to json
   * @return the json string
   */
  public static final String toJson(Object object) {
    return gson.toJson(object);
  }

  /**
   * Returns a JSON response transformer as function reference.
   * 
   * @return ResponseTransformer
   */
  public static final ResponseTransformer jsonTransformer() {
    return JsonUtil::toJson;
  }
}
