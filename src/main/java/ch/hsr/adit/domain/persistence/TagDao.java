package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Tag;


public class TagDao extends GenericDao<Tag, Long> {
  
  private static final Logger LOGGER = Logger.getLogger(TagDao.class);
  
  public TagDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public List<Tag> getFiltered(String name) {
    try {
      LOGGER.info("Try to fetch filtered tags");
      sessionFactory.getCurrentSession().beginTransaction();
      
      String queryString = "SELECT DISTINCT t FROM Tag as t WHERE lower(t.name) LIKE :name";
      Query<Tag> query = createQuery(queryString);
      query.setParameter("name", "%" + name.toLowerCase() + "%");
      List<Tag> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch filtered tags. Transaction rolled back.");
      throw e;
    }
  }


}
