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

  public static final ExceptionHandler EXCEPTIONS =
      (Exception e, Request request, Response response) -> {
        LOGGER.error("Exception occured with message: " + getExceptionMessageChain(e));

        ExceptionUtil util = ExceptionUtil.getInstance();
        int statusCode = util.getHttpErrorCode(e.getClass().getSimpleName());
        response.status(statusCode);

        LOGGER.error(e.getClass().getSimpleName() + " mapped to " + statusCode);

        response.body(toJson(e));
      };

  private static final String getExceptionMessageChain(Throwable throwable) {
    StringBuilder builder = new StringBuilder();
    Throwable root = throwable;
    while (root != null) {
      builder.append(root.getMessage());
      root = root.getCause();
      if (root != null) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  public static final Route NOT_FOUND = (Request request, Response response) -> {
    LOGGER.error("404, page not found:: " + request.url());
    response.status(404);
    return response;
  };

  public static final Route INERNAL_SERVER_ERROR = (Request request, Response response) -> {
    LOGGER.error("500, internal server error: ");
    response.status(500);
    return response;
  };

  public static final Route CORS = (Request request, Response response) -> {
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
