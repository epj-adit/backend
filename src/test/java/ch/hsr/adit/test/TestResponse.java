package ch.hsr.adit.test;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


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
  public Map<String, String>[] jsonList() {
    return new Gson().fromJson(body, HashMap[].class);
  }
}
