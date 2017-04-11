package ch.hsr.adit.application.app;

import static ch.hsr.adit.util.JsonUtil.toJson;

import org.apache.log4j.Logger;

import ch.hsr.adit.util.ExceptionUtil;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class AppHandler {

  private static final Logger LOGGER = Logger.getLogger(AppHandler.class);

  public ExceptionHandler exceptionHandler = (Exception e, Request request, Response response) -> {
    LOGGER.error("Exception occured with message: " + getExceptionMessageChain(e));

    ExceptionUtil util = ExceptionUtil.getInstance();
    int statusCode = util.getHttpErrorCode(e.getClass().getSimpleName());
    response.status(statusCode);

    LOGGER.error(e.getClass().getSimpleName() + " mapped to " + statusCode);

    response.body(toJson(e));
  };

  private String getExceptionMessageChain(Throwable throwable) {
    StringBuilder builder = new StringBuilder();
    while (throwable != null) {
      builder.append(throwable.getMessage());
      throwable = throwable.getCause();
      if (throwable != null) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }

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

  public Route handlerCors = (Request request, Response response) -> {
    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    if (accessControlRequestHeaders != null) {
      response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    }

    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    if (accessControlRequestMethod != null) {
      response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    }
    return "OK";
  };

}
