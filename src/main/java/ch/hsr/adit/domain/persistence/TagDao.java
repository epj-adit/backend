package ch.hsr.adit.domain.persistence;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Tag;


public class TagDao extends GenericDao<Tag, Long> {

  private static final Logger LOGGER = Logger.getLogger(TagDao.class);

  public TagDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
