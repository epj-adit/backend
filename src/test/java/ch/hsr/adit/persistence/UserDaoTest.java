package ch.hsr.adit.persistence;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.GenericDao;
import ch.hsr.adit.domain.persistence.UserDao;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoTest {

  @Mock
  private SessionFactory sessionFactory;
  
  @Mock
  private Session currentSession;

  @Mock
  private Transaction transaction;
  
  
  @Before
  public void setUp() {
    when(sessionFactory.getCurrentSession()).thenReturn(currentSession);
    when(currentSession.getTransaction()).thenReturn(transaction);
  }
  
  @Test(expected = HibernateException.class)
  public void deleteTestFail() {
    GenericDao<User, Long> userDao = new UserDao(sessionFactory);
    
    userDao.delete(2L);
    verify(currentSession).beginTransaction();
  }
}
