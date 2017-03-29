package ch.hsr.adit.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class HibernateUtilTest {

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
