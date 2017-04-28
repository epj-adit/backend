package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.domain.model.Role;

public class RoleController {

  /**
   * API Controller for /role requests.
   * 
   * @param RoleService class
   */
  public RoleController(RoleService roleService) {

    // create
    post(RestApi.Role.ROLE, (request, response) -> {
      Role role = roleService.transformToRole(request);
      return roleService.createRole(role);
    }, jsonTransformer());

    // read
    get(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return roleService.get(id);
    }, jsonTransformer());

    // update
    put(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      Role role = roleService.transformToRole(request);
      long id = Long.parseLong(request.params(":id"));
      role.setId(id);
      return roleService.updateRole(role);
    }, jsonTransformer());

    // delete
    delete(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      // TODO check for permisisons
      long id = Long.parseLong(request.params(":id"));
      return roleService.deleteRole(id);
    }, jsonTransformer());
  }
}
