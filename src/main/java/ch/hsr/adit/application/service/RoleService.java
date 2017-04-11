package ch.hsr.adit.application.service;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.persistence.RoleDao;


public class RoleService {

  private final RoleDao roleDao;

  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  public Role getRole(String name) {
    return roleDao.getByName(name);
  }

  public Role getRole(Long id) {
    return roleDao.get(id);
  }


}
