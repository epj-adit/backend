package ch.hsr.adit.test;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;


public class TestResponse {

  public final String body;
  public final int statusCode;

  public TestResponse(int statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> json() {
    return new Gson().fromJson(body, HashMap.class);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Object>[] jsonList() {
    return new Gson().fromJson(body, HashMap[].class);
  }
}
