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
      Configuration hibernateConfiguration = loadConfiguration(new EnvironmentUtil());
      sessionFactory = hibernateConfiguration.buildSessionFactory();
    } catch (HibernateException ex) {
      logger.error("SessionFactory creation failed. " + ex);
    }
  }
  
  public static Configuration loadConfiguration(EnvironmentUtil environmentUtil) {
    Configuration hibernateConfiguration = new Configuration();
    hibernateConfiguration = hibernateConfiguration.configure("hibernate.cfg.xml");

    // Overwrite default values if there are no environment specific settings defined.
    String dbUser = environmentUtil.getEnvVariable("POSTGRES_USER");
    String dbPassword = environmentUtil.getEnvVariable("POSTGRES_PASSWORD");
    String jdbcUrl = environmentUtil.getEnvVariable("POSTGRES_URL");

    if (dbUser != null) {
      hibernateConfiguration.setProperty("hibernate.connection.username", dbUser);
    }
    if (dbPassword != null) {
      hibernateConfiguration.setProperty("hibernate.connection.password", dbPassword);
    }
    if (jdbcUrl != null) {
      hibernateConfiguration.setProperty("hibernate.connection.url", jdbcUrl);
    }
    
    return hibernateConfiguration;
  }

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      loadConfiguration(new EnvironmentUtil());
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
