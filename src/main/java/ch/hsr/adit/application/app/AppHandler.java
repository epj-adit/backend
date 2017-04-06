package ch.hsr.adit.application.app;

import static ch.hsr.adit.util.JsonUtil.toJson;

import org.apache.log4j.Logger;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class AppHandler {
  
  private static final Logger LOGGER = Logger.getLogger(AppHandler.class);

  public ExceptionHandler exceptionHandler = (Exception e, Request request, Response response) -> {
    LOGGER.error("Adit Exception occured: " + e.getMessage());
    response.status(200);
    response.body(toJson(e));
  };
  
  public Route notFound = (Request request, Response response) -> {
    LOGGER.error("404, page not found:: " + request.url());
    response.status(404);
    return response;
  };
  
  public Route internalServerError = (Request request, Response response) -> {
    LOGGER.error("500, internal server error: ");
    response.status(500);
    return response;
  };

}
