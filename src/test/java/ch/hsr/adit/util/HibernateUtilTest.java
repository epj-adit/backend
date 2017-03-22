package ch.hsr.adit.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateUtilTest {

  private SessionFactory factory;

  @Before
  public void setUp() {
    HibernateUtil.setSessionFactory(factory);
  }

  @After
  public void tearDown() {
    HibernateUtil.setSessionFactory(null);
  }

  @Test
  public void buildSessionFactoryTest() {
    assertNotNull(HibernateUtil.getSessionFactory());
    assertTrue(HibernateUtil.getSessionFactory().isOpen());
  }

  @Test
  public void shutdownTest() {
    HibernateUtil.shutdown();
    assertTrue(HibernateUtil.getSessionFactory().isClosed());
  }
}
