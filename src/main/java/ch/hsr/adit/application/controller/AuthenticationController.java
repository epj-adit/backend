package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.post;

import ch.hsr.adit.application.controller.api.RestApi;
import ch.hsr.adit.application.service.AuthenticationService;

public class AuthenticationController {

  /**
   * API Controller for /authentication requests.
   * 
   * @param userService service class
   */
  public AuthenticationController(AuthenticationService authenticationService) {

    // authentication
    post(RestApi.App.AUTHENTICATE, (request, response) -> {
      return authenticationService.authenticate(request);
    }, jsonTransformer());

  }
}
