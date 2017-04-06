package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Advertisement;

public class AdvertisementDao extends GenericDao<Advertisement, Long> {

  public AdvertisementDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
