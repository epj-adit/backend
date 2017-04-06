package ch.hsr.adit.application.app;

import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import ch.hsr.adit.application.controller.UserController;
import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.exception.SystemException;
import ch.hsr.adit.domain.persistence.RoleDao;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.HibernateUtil;
import ch.hsr.adit.util.KeyStore;

public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class);
  private static final File KEY_FILE = new File("KeyStore.properties");

  public static void main(String[] args) {
    
    // Authentication key store
    setupKeyStore();

    // General app filter
    AppFilter appFilter = new AppFilter();
    before(RestApi.App.WILDCARD, appFilter.handleAuthentication);
    after(RestApi.App.WILDCARD, appFilter.setEncoding);
    
    // General handler for exceptions and errors
    AppHandler appHandler = new AppHandler();
    exception(SystemException.class, appHandler.exceptionHandler);
    notFound(appHandler.notFound);
    internalServerError(appHandler.internalServerError);

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Role
    RoleDao roleDao = new RoleDao(sessionFactory);
    RoleService roleService = new RoleService(roleDao);

    // User
    UserDao userDao = new UserDao(sessionFactory);
    UserService userService = new UserService(userDao, roleService);
    new UserController(userService);
    
    // wait for jetty
    awaitInitialization();
  }

  private static void setupKeyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance();
      keyStore.generateKey(KEY_FILE);
    } catch (NoSuchAlgorithmException | IOException e) {
      LOGGER.error("Cannot instantiate keystore: " + e.getMessage());
    }
  }

}
