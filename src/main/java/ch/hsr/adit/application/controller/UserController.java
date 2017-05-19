package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.log4j.Logger;

import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.exception.ForbiddenException;
import ch.hsr.adit.domain.model.User;
import spark.Request;

public class UserController {
 
  private static final Logger LOGGER = Logger.getLogger(UserController.class);

  private static final String USERS_ROUTE = "/users/";
  private static final String USER_BY_ID_ROUTE = "/user/:id";
  public static final String REGISTER_ROUTE = "/register";
  
  private final PermissionService permissionService;


  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public UserController(UserService userService, PermissionService permissionService) {
    this.permissionService = permissionService;

    // create
    post(REGISTER_ROUTE, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.createUser(user);
    }, jsonTransformer());

    // read
    get(USER_BY_ID_ROUTE, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      User user = userService.get(id);

      checkBasicPermissions(user, getToken(request));

      return user;
    }, jsonTransformer());

    get(USERS_ROUTE, (request, response) -> userService.getAll(), jsonTransformer());

    // update
    put(USER_BY_ID_ROUTE, (request, response) -> {
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
    delete(USER_BY_ID_ROUTE, (request, response) -> {
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
