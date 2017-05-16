package ch.hsr.adit.application.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

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
    try {
      // may throws ObjectNotFoundException
      roleDao.get(role.getId());
   
      return roleDao.update(role);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Role with id " + role.getId() + " not found. Nothing updated");
      throw e;
    } 
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
  
  public List<Role> getAll() {
    return roleDao.getAll();
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
 