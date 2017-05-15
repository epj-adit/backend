package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.log4j.Logger;

import ch.hsr.adit.application.app.RestApi;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.model.exception.ForbiddenException;
import spark.Request;

public class UserController {
  private static final Logger LOGGER = Logger.getLogger(UserController.class);
  private final PermissionService permissionService;

  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public UserController(UserService userService, PermissionService permissionService) {
    this.permissionService = permissionService;

    // create
    post(RestApi.User.REGISTER, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.createUser(user);
    }, jsonTransformer());

    // read
    get(RestApi.User.USER_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      User user = userService.get(id);

      checkBasicPermissions(user, getToken(request));

      return user;
    }, jsonTransformer());

    get(RestApi.User.USERS_FILTERED, (request, response) -> {
      // TODO: check admin permissions
      return userService.getAllFiltered(request);
    }, jsonTransformer());

    // update
    put(RestApi.User.USER_BY_ID, (request, response) -> {
      User user = userService.transformToUser(request);
      long id = Long.parseLong(request.params(":id"));
      user.setId(id);

      // check if user exists
      userService.get(user);

      checkBasicPermissions(user, getToken(request));
      checkEditIsActivePermission(getToken(request));

      return userService.updateUser(user);
    }, jsonTransformer());

    // delete
    delete(RestApi.User.USER_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      User user = userService.get(id);

      checkBasicPermissions(user, getToken(request));

      return userService.deleteUser(id);
    }, jsonTransformer());
  }

  private void checkBasicPermissions(User user, String token) {
    if (!permissionService.checkBasicPermissions(user, token)) {
      LOGGER.warn("User does not have permission to access User with id " + user.getId() + " !");
      throw new ForbiddenException(
          "User does not have permission to access User with id " + user.getId() + " !");
    }
  }

  private void checkEditIsActivePermission(String token) {
    if (!permissionService.checkEditIsActivePermission(token)) {
      LOGGER.warn("Additional permission is needed for editing other users status'!");
      throw new ForbiddenException(
          "Additional permission is needed for editing other users status'!");
    }
  }

  private String getToken(Request request) {
    return request.headers("Authorization");
  }

}
