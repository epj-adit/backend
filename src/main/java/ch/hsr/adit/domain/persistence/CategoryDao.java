package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;

import ch.hsr.adit.domain.model.Category;


public class CategoryDao extends GenericDao<Category, Long> {

  public CategoryDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
