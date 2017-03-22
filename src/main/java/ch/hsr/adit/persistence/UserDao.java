package ch.hsr.adit.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.model.User;


public class UserDao extends GenericDao<User, Long> {

  public UserDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public User getUserByEmail(String email) {
    startTransaction();
    Query<User> userQuery = createQuery("from User u where u.email = :email");
    userQuery.setParameter("email", email);

    User user = userQuery.getSingleResult();
    endTransaction();

    return user;
  }

}
