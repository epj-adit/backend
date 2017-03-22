package ch.hsr.adit.business.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.business.service.UserService;
import ch.hsr.adit.model.User;

public class UserController {

  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public UserController(UserService userService) {

    // create
    post("/user", (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.createUser(user);
    }, jsonTransformer());

    // read
    get("/user/:id", (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return userService.get(id);
    }, jsonTransformer());

    get("/users", (request, response) -> {
      return userService.getAll();
    }, jsonTransformer());

    // update
    put("user/:id", (request, response) -> {
      User user = userService.transformToUser(request);
      return userService.updateUser(user);
    }, jsonTransformer());

    // delete
    delete("user/:id", (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return userService.deleteUser(id);
    }, jsonTransformer());
  }
}
