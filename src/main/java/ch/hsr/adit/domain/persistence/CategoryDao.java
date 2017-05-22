package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Category;


public class CategoryDao extends GenericDao<Category> {
  
  private static final Logger LOGGER = Logger.getLogger(CategoryDao.class);

  public CategoryDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<Category> getFiltered(String name) {
    try {
      LOGGER.info("Try to fetch filtered categories");
      sessionFactory.getCurrentSession().beginTransaction();
      
      String queryString = "SELECT DISTINCT c FROM Category as c WHERE lower(c.name) LIKE :name";
      Query<Category> query = createQuery(queryString);
      query.setParameter("name", "%" + name.toLowerCase() + "%");
      List<Category> result = query.getResultList();
      
      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch filtered categories. Transaction rolled back.");
      throw e;
    }
  }

}
