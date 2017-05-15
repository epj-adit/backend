package ch.hsr.adit.application.app;


import javax.naming.AuthenticationException;

import ch.hsr.adit.util.TokenUtil;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

public class AppFilter {

  public static final Filter AUTHENTICATION = (Request request, Response response) -> {
    if (needsAuthentication(request)) {
      String token = request.headers("Authorization");
      if (token == null) {
        throw new AuthenticationException("No token provided!");
      }
      if (!TokenUtil.getInstance().verify(token)) {
        // throws HaltException
        throw Spark.halt();
      }
    }
  };

  public static final Filter ENCODING = (Request request, Response response) -> {
    response.type("application/json");
    response.header("Content-Encoding", "gzip");
  };

  public static final Filter CORS_ORIGIN = (Request request, Response response) -> {
    response.header("Access-Control-Allow-Origin", "*");
  };
  
  private static final boolean needsAuthentication(Request request) {
    return (!"OPTIONS".equalsIgnoreCase(request.requestMethod())
        && !(request.uri().startsWith(RestApi.App.AUTHENTICATE)
        || request.uri().startsWith(RestApi.User.REGISTER)));
  }

}
