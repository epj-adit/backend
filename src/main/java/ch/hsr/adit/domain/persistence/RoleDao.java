package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Role;


public class RoleDao extends GenericDao<Role, Long> {

  public RoleDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
