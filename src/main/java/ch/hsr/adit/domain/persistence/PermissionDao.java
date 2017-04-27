package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Permission;


public class PermissionDao extends GenericDao<Permission, Long> {
  
  public PermissionDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
