package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.User;


public class UserDao extends GenericDao<User, Long> {

  private static final Logger LOGGER = Logger.getLogger(UserDao.class);

  public UserDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public User getUserByEmail(String email) {
    try {
      LOGGER.info("Try to fetch user with email " + email);
      sessionFactory.getCurrentSession().beginTransaction();
      Query<User> userQuery = createQuery("SELECT DISTINCT u FROM User u WHERE u.email = :email");
      userQuery.setParameter("email", email);
      User user = userQuery.getSingleResult();
      if (user == null) {
        throw new HibernateException("user with email " + email + " not found");
      }
      sessionFactory.getCurrentSession().getTransaction().commit();
      return user;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to persist user. Transaction rolled back.");
      throw e;
    }
  }
}
