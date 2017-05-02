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
import ch.hsr.adit.application.controller.MediaController;
import ch.hsr.adit.application.controller.MessageController;
import ch.hsr.adit.application.controller.PermissionController;
import ch.hsr.adit.application.controller.RoleController;
import ch.hsr.adit.application.controller.SubscriptionController;
import ch.hsr.adit.application.controller.TagController;
import ch.hsr.adit.application.controller.UserController;
import ch.hsr.adit.application.service.AdvertisementService;
import ch.hsr.adit.application.service.AuthenticationService;
import ch.hsr.adit.application.service.CategoryService;
import ch.hsr.adit.application.service.MediaService;
import ch.hsr.adit.application.service.MessageService;
import ch.hsr.adit.application.service.PermissionService;
import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.application.service.SubscriptionService;
import ch.hsr.adit.application.service.TagService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.persistence.AdvertisementDao;
import ch.hsr.adit.domain.persistence.CategoryDao;
import ch.hsr.adit.domain.persistence.MediaDao;
import ch.hsr.adit.domain.persistence.MessageDao;
import ch.hsr.adit.domain.persistence.PermissionDao;
import ch.hsr.adit.domain.persistence.RoleDao;
import ch.hsr.adit.domain.persistence.SubscriptionDao;
import ch.hsr.adit.domain.persistence.TagDao;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.util.HibernateUtil;
import ch.hsr.adit.util.KeyStore;

public class App {

  private static final Logger LOGGER = Logger.getLogger(App.class);
  private static final File KEY_FILE = new File("KeyStore.properties");

  public static void main(String[] args) {

    /***
     *
     * APP FILTER
     * 
     */

    // Authentication key store
    setupKeyStore();

    // General app filter
    AppFilter appFilter = new AppFilter();
    before(RestApi.App.WILDCARD, appFilter.handleAuthentication);
    after(RestApi.App.WILDCARD, appFilter.setCorsOrigin);
    after(RestApi.App.WILDCARD, appFilter.setEncoding);

    // General handler for exceptions and errors
    AppHandler appHandler = new AppHandler();
    exception(Exception.class, appHandler.exceptionHandler);
    notFound(appHandler.notFound);
    internalServerError(appHandler.internalServerError);

    // CORS
    options(RestApi.App.WILDCARD, appHandler.handlerCors);


    /***
     *
     * DEPENDENCY INJECTION
     * 
     */

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    // Message
    MessageDao messageDao = new MessageDao(sessionFactory);
    MessageService messageService = new MessageService(messageDao);
    new MessageController(messageService);

    // User
    UserDao userDao = new UserDao(sessionFactory);
    UserService userService = new UserService(userDao, messageDao);
    new UserController(userService);
    
    // Authenticate
    AuthenticationService authenticationService = new AuthenticationService(userDao);
    new AuthenticationController(authenticationService);

    // User
    AdvertisementDao advertisementDao = new AdvertisementDao(sessionFactory);
    AdvertisementService advertisementService = new AdvertisementService(advertisementDao);
    new AdvertisementController(advertisementService);

    // Media
    MediaDao mediaDao = new MediaDao(sessionFactory);
    MediaService mediaService = new MediaService(mediaDao, advertisementService);
    new MediaController(mediaService);

    // Tag
    TagDao tagDao = new TagDao(sessionFactory);
    TagService tagService = new TagService(tagDao, advertisementDao);
    new TagController(tagService);

    // Category
    CategoryDao categoryDao = new CategoryDao(sessionFactory);
    CategoryService categoryService = new CategoryService(categoryDao);
    new CategoryController(categoryService);

    // Permission
    PermissionDao permissionDao = new PermissionDao(sessionFactory);
    PermissionService permissionService = new PermissionService(permissionDao);
    new PermissionController(permissionService);

    // Subscription
    SubscriptionDao subscriptionDao = new SubscriptionDao(sessionFactory);
    SubscriptionService subscriptionService = new SubscriptionService(subscriptionDao);
    new SubscriptionController(subscriptionService);

    // Role
    RoleDao roleDao = new RoleDao(sessionFactory);
    RoleService roleService = new RoleService(roleDao);
    new RoleController(roleService);

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
