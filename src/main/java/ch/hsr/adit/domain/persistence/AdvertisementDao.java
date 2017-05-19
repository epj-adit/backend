package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Advertisement;
import ch.hsr.adit.domain.model.filter.AdvertisementFilter;

public class AdvertisementDao extends GenericDao<Advertisement> {

  private static final Logger LOGGER = Logger.getLogger(AdvertisementDao.class);

  public AdvertisementDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Creates a case insensitive filter query for advertisements. Title and description are compared
   * alike.
   * 
   * @param title full or part of the title
   * @param description full or part of the description
   * @param userId id of the related user
   * @param categoryIds array of category ids
   * @param tagIds array of tag ids
   * @return List of advertisement
   */
  public List<Advertisement> get(AdvertisementFilter filter) {
    LOGGER.info("Try to fetch filtered advertisements");

    String query = createQueryString(filter);
    return executeQuery(query, filter);
  }

  private String createQueryString(AdvertisementFilter filter) {

    StringBuilder queryString =
        new StringBuilder("SELECT DISTINCT a FROM Advertisement as a WHERE ");

    // optional join
    if (filter.getTagIds() != null && !filter.getTagIds().isEmpty()) {
      queryString.insert(queryString.indexOf("WHERE"), " JOIN a.tags as t ");
    }

    if (filter.getTitle() != null) {
      if (filter.getDescription() != null) {
        queryString.append(" (");
      }
      queryString.append("and lower(a.title) LIKE :title ");
    }
    if (filter.getDescription() != null) {
      queryString.append("or lower(a.description) LIKE :description ");
      if (filter.getTitle() != null) {
        queryString.append(") ");
      }
    }
    if (filter.getUserId() != null) {
      queryString.append("and a.user.id = :userId ");
    }

    if (!filter.getAdvertisementStates().isEmpty()) {
      queryString.append("and a.advertisementState IN (:advertisementStates) ");
    }

    if (!filter.getCategoryIds().isEmpty()) {
      queryString.append("and a.category.id IN (:categoryIds) ");
    }

    if (!filter.getTagIds().isEmpty()) {
      queryString.append("and t.id IN (:tagIds) ");
    }

    // remove first "AND"
    int index = queryString.indexOf("and");
    if (index == -1) {
      index = queryString.indexOf("or");
    }
    if (index != -1) {
      queryString.replace(index, index + 3, "");
    }

    return queryString.toString();
  }

  private List<Advertisement> executeQuery(String queryString, AdvertisementFilter filter) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();

      // set parameter
      Query<Advertisement> query = createQuery(queryString);
      if (filter.getTitle() != null) {
        query.setParameter("title", "%" + filter.getTitle().toLowerCase() + "%");
      }
      if (filter.getDescription() != null) {
        query.setParameter("description", "%" + filter.getDescription().toLowerCase() + "%");
      }
      if (filter.getUserId() != null) {
        query.setParameter("userId", filter.getUserId());
      }

      if (!filter.getAdvertisementStates().isEmpty()) {
        query.setParameter("advertisementStates", filter.getAdvertisementStates());
      }

      if (!filter.getCategoryIds().isEmpty()) {
        query.setParameter("categoryIds", filter.getCategoryIds());
      }

      if (!filter.getTagIds().isEmpty()) {
        query.setParameter("tagIds", filter.getTagIds());
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
      final String queryString =
          "SELECT DISTINCT a FROM Advertisement as a JOIN a.tags as t WHERE t.name = :name";
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
