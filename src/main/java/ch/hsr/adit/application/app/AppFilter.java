package ch.hsr.adit.application.app;


import javax.naming.AuthenticationException;

import ch.hsr.adit.application.controller.AuthenticationController;
import ch.hsr.adit.application.controller.UserController;
import ch.hsr.adit.util.TokenUtil;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

public class AppFilter {

  private AppFilter() {
    throw new IllegalAccessError("Utility class");
  }

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

  public static final Filter CORS = (Request request, Response response) -> {
    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    if (accessControlRequestHeaders != null) {
      response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    }

    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    if (accessControlRequestMethod != null) {
      response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    }

    response.header("Access-Control-Allow-Origin", "*");
  };

  public static final Filter ENCODING = (Request request, Response response) -> {
    response.type("application/json");
    response.header("Content-Encoding", "gzip");
  };

  private static final boolean needsAuthentication(Request request) {
    return !"OPTIONS".equalsIgnoreCase(request.requestMethod())
        && !(request.uri().startsWith(AuthenticationController.AUTHENTICATE_ROUTE)
            || request.uri().startsWith(UserController.REGISTER_ROUTE));
  }

}
