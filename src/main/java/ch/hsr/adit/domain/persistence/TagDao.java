package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Tag;


public class TagDao extends GenericDao<Tag> {

  public TagDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
