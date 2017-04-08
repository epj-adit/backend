package ch.hsr.adit.domain.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.exception.DatabaseError;
import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.model.User;


public class UserDao extends GenericDao<User, Long> {

  public UserDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public User getUserByEmail(String email) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      Query<User> userQuery = createQuery("from User u where u.email = :email");
      userQuery.setParameter("email", email);

      User user = userQuery.getSingleResult();
      sessionFactory.getCurrentSession().getTransaction().commit();
      return user;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.GENERIC_DATABASE, e);
    }
  }

}
