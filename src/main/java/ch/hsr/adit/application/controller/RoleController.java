package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.apache.log4j.Logger;

import ch.hsr.adit.application.app.RestApi;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.exception.ForbiddenException;
import spark.Request;

public class RoleController {
  private static final Logger LOGGER = Logger.getLogger(RoleController.class);
  private final PermissionService permissionService;
  
  /**
   * API Controller for /role requests.
   * 
   * @param RoleService class
   */
  public RoleController(RoleService roleService, PermissionService permissionService) {
    this.permissionService = permissionService;
    // create
    post(RestApi.Role.ROLE, (request, response) -> {
      checkEditRolesPermission(getToken(request));
      
      Role role = roleService.transformToRole(request);
      return roleService.createRole(role);
    }, jsonTransformer());

    // read
    get(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return roleService.get(id);
    }, jsonTransformer());
    
    get(RestApi.Role.ROLES, (request, response) -> {
      return roleService.getAll();
    }, jsonTransformer());

    // update
    put(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      Role role = roleService.transformToRole(request);
      long id = Long.parseLong(request.params(":id"));
      role.setId(id);
      
      // check if role exists
      roleService.get(id);
      checkEditRolesPermission(getToken(request));
      
      return roleService.updateRole(role);
    }, jsonTransformer());

    // delete
    delete(RestApi.Role.ROLE_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      
      
      // check if role exists
      roleService.get(id);
      checkEditRolesPermission(getToken(request));
      
      return roleService.deleteRole(id);
    }, jsonTransformer());
  }
  
  private void checkEditRolesPermission(String token) {
    if (!permissionService.checkEditRolesPermission(token)) {
      LOGGER.warn("User does not have permission to edit roles!");
      throw new ForbiddenException("User does not have permission to edit roles!");
    }
  }
  
  private String getToken(Request request) {
    return request.headers("Authorization");
  }
}
