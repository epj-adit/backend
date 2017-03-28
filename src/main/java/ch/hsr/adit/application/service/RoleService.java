package ch.hsr.adit.application.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.persistence.RoleDao;


public class RoleService {

  private static final Logger logger = Logger.getLogger(RoleService.class);
  
  private final RoleDao roleDao;

  public RoleService(RoleDao roleDao) {
    this.roleDao = roleDao;
  }
  
  public Role getRole(String name) {
    try {
      return roleDao.getByName(name);
    } catch (HibernateException e) {
      logger.error("HibernateException occured while find role by name: " + e.getMessage());
      return null;
    }
  }
  
  public Role getRole(Long id) {
    try {
      return roleDao.get(id);
    } catch (HibernateException e) {
      logger.error("HibernateException occured while find role by name: " + e.getMessage());
      return null;
    }
  }


}
