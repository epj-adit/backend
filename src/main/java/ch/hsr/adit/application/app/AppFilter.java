package ch.hsr.adit.application.app;


import static spark.Spark.halt;

import ch.hsr.adit.util.AuthenticationUtil;
import spark.Filter;
import spark.Request;
import spark.Response;

public class AppFilter {
  
  public Filter handleAuthentication = (Request request, Response response) -> {
    if (!AuthenticationUtil.authenticated("test", "secure")) {
      halt(401, "You are not welcome here");
    }
  };

  public Filter setEncoding = (Request request, Response response) -> {
    response.type("application/json");
    response.header("Content-Encoding", "gzip");
    // TODO: send jwt on successfull auth
    // response.header("Authorization", "Bearer " + jwt");
  };
  
}
