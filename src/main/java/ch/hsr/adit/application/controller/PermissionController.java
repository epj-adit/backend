package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.domain.model.Permission;

public class PermissionController {

  /**
   * API Controller for /permission requests.
   * 
   * @param permissionService service class
   */
  public PermissionController(PermissionService permissionService) {

    // create
    post(RestApi.Permission.PERMISSION, (request, response) -> {
      Permission permission = permissionService.transformToPermission(request);
      return permissionService.createPermission(permission);
    }, jsonTransformer());

    // read
    get(RestApi.Permission.PERMISSION_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return permissionService.get(id);
    }, jsonTransformer());

    get(RestApi.Permission.PERMISSIONS, (request, response) -> {
      return permissionService.getAll();
    }, jsonTransformer());
    
    // update
    put(RestApi.Permission.PERMISSION_BY_ID, (request, response) -> {
      Permission permission = permissionService.transformToPermission(request);
      return permissionService.updatePermission(permission);
    }, jsonTransformer());

    // delete
    delete(RestApi.Permission.PERMISSION_BY_ID, (request, response) -> {
      // TODO check for permisisons
      long id = Long.parseLong(request.params(":id"));
      return permissionService.deletePermission(id);
    }, jsonTransformer());
  }
}
