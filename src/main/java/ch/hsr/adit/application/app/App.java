package ch.hsr.adit.application.app;

import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.options;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import ch.hsr.adit.application.controller.AdvertisementController;
import ch.hsr.adit.application.controller.AuthenticationController;
import ch.hsr.adit.application.controller.CategoryController;
import ch.hsr.adit.application.controller.RoleController;
import ch.hsr.adit.application.controller.TagController;
import ch.hsr.adit.application.controller.UserController;
import ch.hsr.adit.application.service.AdvertisementService;
import ch.hsr.adit.application.service.AuthenticationService;
import ch.hsr.adit.application.service.CategoryService;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.application.service.TagService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.persistence.AdvertisementDao;
import ch.hsr.adit.domain.persistence.CategoryDao;
import ch.hsr.adit.domain.persistence.RoleDao;
import ch.hsr.adit.domain.persistence.TagDao;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.HibernateUtil;
import ch.hsr.adit.util.KeyStore;

public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class);
  private static final File KEY_FILE = new File("KeyStore.properties");
  
  private static final String WILDCARD_ROUTE = "/*";

  public static void main(String[] args) {

    /***
     *
     * APP FILTER
     * 
     */

    // Authentication key store
    setupKeyStore();

    // General app filter
    before(WILDCARD_ROUTE, AppFilter.CORS);
    before(WILDCARD_ROUTE, AppFilter.AUTHENTICATION);
    after(WILDCARD_ROUTE, AppFilter.ENCODING);

    // General handler for exceptions and errors
    exception(Exception.class, AppHandler.EXCEPTION);
    notFound(AppHandler.NOT_FOUND);
    internalServerError(AppHandler.INERNAL_SERVER_ERROR);

    // Map Options Calls
    options(WILDCARD_ROUTE, AppHandler.CORS);

    /***
     *
     * DEPENDENCY INJECTION
     * 
     */

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // User
    UserDao userDao = new UserDao(sessionFactory);
    UserService userService = new UserService(userDao);


    // Permission
    PermissionService permissionService = new PermissionService(userService);

    new UserController(userService, permissionService);

    // Authenticate
    AuthenticationService authenticationService = new AuthenticationService(userDao);
    new AuthenticationController(authenticationService);

    // User
    AdvertisementDao advertisementDao = new AdvertisementDao(sessionFactory);
    AdvertisementService advertisementService = new AdvertisementService(advertisementDao);
    new AdvertisementController(advertisementService, permissionService);

    // Tag
    TagDao tagDao = new TagDao(sessionFactory);
    TagService tagService = new TagService(tagDao, advertisementDao);
    new TagController(tagService);

    // Category
    CategoryDao categoryDao = new CategoryDao(sessionFactory);
    CategoryService categoryService = new CategoryService(categoryDao);
    new CategoryController(categoryService, permissionService);

    // Role
    RoleDao roleDao = new RoleDao(sessionFactory);
    RoleService roleService = new RoleService(roleDao);
    new RoleController(roleService, permissionService);

    // wait for jetty
    awaitInitialization();
  }

  private static void setupKeyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance();
      keyStore.generateKey(KEY_FILE);
    } catch (NoSuchAlgorithmException | IOException e) {
      LOGGER.error("Cannot instantiate keystore");
      LOGGER.error(e);
    }
  }

}
