package ch.hsr.adit.application.service;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.persistence.RoleDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class RoleService {

  private static final Logger LOGGER = Logger.getLogger(RoleService.class);
  private final RoleDao roleDao;

  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }
  
  public Role createRole(Role role) {
    return (Role) roleDao.persist(role);
  }

  public Role updateRole(Role role) {
    return roleDao.update(role);
  }

  public boolean deleteRole(Role roleToDelete) {
    roleDao.delete(roleToDelete);
    return true;
  }

  public boolean deleteRole(long id) {
    Role role = get(id);
    deleteRole(role);
    return true;
  }

  public Role get(String name) {
    return roleDao.getByName(name);
  }

  public Role get(Long id) {
    return roleDao.get(id);
  }
  
  public Role transformToRole(Request request) {
    try {
      Role role = JsonUtil.fromJson(request.body(), Role.class);
      LOGGER.info("Received JSON data: " + role.toString());
      return role;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }


}
