package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Subscription;


public class SubscriptionDao extends GenericDao<Subscription, Long> {
  
  private static final Logger LOGGER = Logger.getLogger(SubscriptionDao.class);
  
  public SubscriptionDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }
  
  public List<Subscription> getFiltered(Long categoryId) {
    try {
      LOGGER.info("Try to fetch filtered subscriptions");
      sessionFactory.getCurrentSession().beginTransaction();
      
      String queryString = "SELECT DISTINCT s FROM Subscription as s "
          + "WHERE s.category.id = :categoryId";
      Query<Subscription> query = createQuery(queryString);
      query.setParameter("categoryId", categoryId);
      List<Subscription> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch filtered subscriptions. Transaction rolled back.");
      throw e;
    }
  }


}
