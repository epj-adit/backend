package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.model.User;

public class UserController {

  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public UserController(UserService userService) {

    // create
    post(RestApi.User.USER, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.createUser(user);
    }, jsonTransformer());

    // read
    get(RestApi.User.USER_BY_ID, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.get(user);
    }, jsonTransformer());

    get(RestApi.User.USERS, (request, response) -> {
      return userService.getAll();
    }, jsonTransformer());

    // update
    put(RestApi.User.USER_BY_ID, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.updateUser(user);
    }, jsonTransformer());

    // delete
    delete(RestApi.User.USER_BY_ID, (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.deleteUser(user);
    }, jsonTransformer());
  }
}
