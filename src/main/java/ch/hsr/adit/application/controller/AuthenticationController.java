package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.get;

import ch.hsr.adit.application.service.AuthenticationService;

public class AuthenticationController {

  /**
   * API Controller for /authentication requests.
   * 
   * @param userService service class
   */
  public AuthenticationController(AuthenticationService authenticationService) {

    // authentication
    get(RestApi.App.AUTHENTICATE, (request, response) -> {
      return authenticationService.authenticate(request);
    }, jsonTransformer());

  }
}
