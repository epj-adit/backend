package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Advertisement;

public class AdvertisementDao extends GenericDao<Advertisement, Long> {

  private static final Logger LOGGER = Logger.getLogger(AdvertisementDao.class);
  
  public AdvertisementDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Creates a case insensitive filter query for advertisements.
   * Title and description are compared alike. 
   * 
   * @param title full or part of the title
   * @param description full or part of the description
   * @param userId id of the related user
   * @param categoryIds array of category ids
   * @param tagIds array of tag ids
   * @return List of advertisement
   */
  public List<Advertisement> get(String title, String description, Long userId,
      List<Long> categoryIds, List<Long> tagIds) {
    
    LOGGER.info("Try to fetch filtered advertisements");

    StringBuilder queryString =
        new StringBuilder("SELECT a FROM Advertisement as a WHERE ");
    
    // optional join
    if (tagIds != null && !tagIds.isEmpty()) {
      queryString.insert(queryString.indexOf("WHERE"), " JOIN a.tags as t ");
    } 
    
    if (title != null) {
      queryString.append("and lower(a.title) LIKE :title ");
    }
    if (description != null) {
      queryString.append("and lower(a.description) LIKE :description ");
    }
    if (userId != null) {
      queryString.append("and a.user.id = :userId ");
    }
    if (categoryIds != null && !categoryIds.isEmpty()) {
      queryString.append("and a.category.id IN (:categoryIds) ");
    }

    if (tagIds != null && !tagIds.isEmpty()) {
      queryString.append("and t.id IN (:tagIds) ");
    }

    // remove first "AND"
    int index = queryString.indexOf("and");
    queryString.replace(index, index + 3, "");
    
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      
      // set parameter
      Query<Advertisement> query = createQuery(queryString.toString());
      if (title != null) {
        query.setParameter("title", "%" + title.toLowerCase() + "%");
      }
      if (description != null) {
        query.setParameter("description", "%" +  description.toLowerCase() + "%");
      }
      if (userId != null) {
        query.setParameter("userId", userId);
      }
      if (categoryIds != null && !categoryIds.isEmpty()) {
        query.setParameter("categoryIds", categoryIds);
      }
      if (tagIds != null && !tagIds.isEmpty()) {
        query.setParameter("tagIds", tagIds);
      }

      List<Advertisement> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch filtered advertisement. Transaction rolled back.");
      throw e;
    }
  }
  
  public List<Advertisement> getByTag(String tagName) {
    try {
      LOGGER.info("Try to fetch advertisements by tag");
      
      sessionFactory.getCurrentSession().beginTransaction();
      final String queryString = "SELECT a FROM Advertisement as a JOIN a.tags as t WHERE t.name = :name";
      Query<Advertisement> query = createQuery(queryString);
      query.setParameter("name", tagName);
      List<Advertisement> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch advertisement by tag. Transaction rolled back.");
      throw e;
    }
  }
}
