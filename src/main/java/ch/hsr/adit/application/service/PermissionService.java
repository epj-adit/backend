package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Permission;
import ch.hsr.adit.domain.persistence.PermissionDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class PermissionService {

  private static final Logger LOGGER = Logger.getLogger(PermissionService.class);
  private final PermissionDao permissionDao;


  public PermissionService(PermissionDao permissionDao) {
    this.permissionDao = permissionDao;
  }

  public Permission createPermission(Permission permission) {
    return (Permission) permissionDao.persist(permission);
  }

  public Permission updatePermission(Permission permission) {
    try {
      // may throws ObjectNotFoundException
      permissionDao.get(permission.getId());

      return permissionDao.update(permission);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Permission with id " + permission.getId() + " not found. Nothing updated");
      throw e;
    }
  }

  public boolean deletePermission(Permission permissionToDelete) {
    permissionDao.delete(permissionToDelete);
    return true;
  }

  public boolean deletePermission(long id) {
    Permission permission = get(id);
    deletePermission(permission);
    return true;
  }

  public Permission get(Permission permission) {
    return get(permission.getId());
  }

  public Permission get(Long id) {
    Permission permission = permissionDao.get(id);
    return permission;
  }

  public List<Permission> getAll() {
    return permissionDao.getAll();
  }

  public Permission transformToPermission(Request request) {
    try {
      Permission permission = JsonUtil.fromJson(request.body(), Permission.class);
      LOGGER.info("Received JSON data: " + permission.toString());
      return permission;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }


}
