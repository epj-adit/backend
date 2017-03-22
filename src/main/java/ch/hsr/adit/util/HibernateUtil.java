package ch.hsr.adit.util;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public final class HibernateUtil {

  private static final Logger logger = Logger.getLogger(HibernateUtil.class);

  private static SessionFactory sessionFactory = null;

  private static void buildSessionFactory() {
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (HibernateException ex) {
      logger.error("SessionFactory creation failed. " + ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      buildSessionFactory();
    }
    return sessionFactory;
  }

  public static void setSessionFactory(SessionFactory sessionFactory) {
    HibernateUtil.sessionFactory = sessionFactory;
  }

  public static void shutdown() {
    // Close caches and connection pools
    getSessionFactory().close();
  }

}
