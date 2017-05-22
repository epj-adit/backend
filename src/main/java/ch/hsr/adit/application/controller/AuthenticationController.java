package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.post;

import ch.hsr.adit.application.service.AuthenticationService;

public class AuthenticationController {

  public static final String AUTHENTICATE_ROUTE = "/authenticate";

  /**
   * API Controller for /authentication requests.
   * 
   * @param userService service class
   */
  public AuthenticationController(AuthenticationService authenticationService) {

    // authentication
    post(AUTHENTICATE_ROUTE, (request, response) -> authenticationService.authenticate(request),
        jsonTransformer());
  }
}
