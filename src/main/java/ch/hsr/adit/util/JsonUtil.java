package ch.hsr.adit.util;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public final class JsonUtil {

  /**
   * Converts the given object to json using Gson.
   * 
   * @param object to serialize to json
   * @return the json string
   */
  public static final String toJson(Object object) {
    return new Gson().toJson(object);
  }

  /**
   * Returns a JSON response transformer as functional interface.
   * 
   * @return ResponseTransformer
   */
  public static final ResponseTransformer jsonTransformer() {
    return JsonUtil::toJson;
  }
}
