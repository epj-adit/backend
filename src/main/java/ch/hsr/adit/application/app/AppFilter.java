package ch.hsr.adit.application.app;


import javax.naming.AuthenticationException;

import ch.hsr.adit.util.TokenUtil;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

public class AppFilter {

  public Filter handleAuthentication = (Request request, Response response) -> {
    if (!request.uri().startsWith(RestApi.App.AUTHENTICATE)) {
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

  public Filter setEncoding = (Request request, Response response) -> {
    response.type("application/json");
    response.header("Content-Encoding", "gzip");
  };

  public Filter setCorsOrigin = (Request request, Response response) -> {
    response.header("Access-Control-Allow-Origin", "*");
  };

}
