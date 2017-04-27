package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HibernateUtilTest {
  
  @Mock
  private EnvironmentUtil environmentUtil;
  
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
  
  @Test
  public void customUserTest() {
    // arrange
    String username = "adit_test";
    when(environmentUtil.getEnvVariable("POSTGRES_USER")).thenReturn(username);

    // act
    Configuration config = HibernateUtil.loadConfiguration(environmentUtil);
    
    // assert
    assertEquals(username, config.getProperty("hibernate.connection.username"));
  }
  
  @Test
  public void customPasswordTest() {
    // arrange
    String password = "newUltraSecure12345";
    when(environmentUtil.getEnvVariable("POSTGRES_PASSWORD")).thenReturn(password);

    // act
    Configuration config = HibernateUtil.loadConfiguration(environmentUtil);
    
    // assert
    assertEquals(password, config.getProperty("hibernate.connection.password"));
  }
  
  @Test
  public void customJdbcUrlTest() {
    // arrange
    String jdbc = "jdbc:postgresql://10.10.10.10:5432/adit_test";
    when(environmentUtil.getEnvVariable("POSTGRES_URL")).thenReturn(jdbc);

    // act
    Configuration config = HibernateUtil.loadConfiguration(environmentUtil);
    
    // assert
    assertEquals(jdbc, config.getProperty("hibernate.connection.url"));
  }
}
