package ch.hsr.adit.test;

import java.util.HashMap;
import java.util.Map;

import ch.hsr.adit.util.JsonUtil;


public class TestResponse {

  public final String body;
  public final int statusCode;

  public TestResponse(int statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> json() {
    return JsonUtil.fromJson(body, HashMap.class);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Object>[] jsonList() {
    return JsonUtil.fromJson(body, HashMap[].class);
  }
}
