package ch.hsr.adit.application.app;


import spark.Filter;
import spark.Request;
import spark.Response;

public class AppFilter {
  
  public Filter handleAuthentication = (Request request, Response response) -> {
    // TODO try to parse Authorization header and verify
    // String token = request.headers("Authorization");
    // if (! TokenUtil.getInstance().verify(token) ) {
    // // throws HaltException
    // throw Spark.halt();
    // }
  };

  public Filter setEncoding = (Request request, Response response) -> {
    response.type("application/json");
    response.header("Content-Encoding", "gzip");
  };

}
