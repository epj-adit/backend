package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Media;


public class MediaDao extends GenericDao<Media, Long> {

  public MediaDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
