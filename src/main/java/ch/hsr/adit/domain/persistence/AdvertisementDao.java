package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.exception.DatabaseError;
import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.model.Advertisement;

public class AdvertisementDao extends GenericDao<Advertisement, Long> {

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

    StringBuilder queryString =
        new StringBuilder("SELECT a FROM Advertisement as a JOIN a.tags as t WHERE ");
    if (title != null) {
      queryString.append("and lower(a.title) LIKE :title ");
    }
    if (description != null) {
      queryString.append("and lower(a.description) LIKE :description ");
    }
    if (userId != null) {
      queryString.append("and a.user.id = :userId ");
    }
    if (categoryIds != null) {
      queryString.append("and a.category.id IN (:categoryIds) ");
    }

    if (tagIds != null) {
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
      if (categoryIds != null) {
        query.setParameter("categoryIds", categoryIds);
      }
      if (tagIds != null) {
        query.setParameter("tagIds", tagIds);
      }

      List<Advertisement> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.GENERIC_DATABASE, e);
    }
  }
}
