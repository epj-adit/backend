package ch.hsr.adit.application.app;

import static ch.hsr.adit.util.JsonUtil.toJson;
import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.halt;
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
import ch.hsr.adit.domain.persistence.RoleDao;
import ch.hsr.adit.domain.persistence.UserDao;
import ch.hsr.adit.exception.SystemException;
import ch.hsr.adit.util.AuthenticationUtil;
import ch.hsr.adit.util.HibernateUtil;
import ch.hsr.adit.util.KeyStore;

public class App {

  private static final Logger logger = Logger.getLogger(App.class);
  private final static File KEY_FILE = new File("KeyStore.properties");

  public static void main(String[] args) {

    setupApiController();

    setupSeverHandler();
    
    setupKeyStore();

    // wait for jetty
    awaitInitialization();
  }
 }

  private static void setupSeverHandler() {
    before((request, response) -> {
      // %TODO get current user // jwt verify
      if (!AuthenticationUtil.authenticated("test", "secure")) {
        halt(401, "You are not welcome here");
      }
    });

    after((request, response) -> {
      response.type("application/json");
      response.header("Content-Encoding", "gzip");
      // TODO: send jwt on successfull auth
      // response.header("Authorization", "Bearer " + jwt");
    });

    exception(SystemException.class, (e, req, res) -> {
      logger.error("Adit Exception occured: " + e.getMessage());

      res.status(200);
      res.body(toJson(e));
    });

    notFound((req, res) -> {
      logger.error("404, page not found:: " + req.url());
      res.status(404);
      return res;
    });

    internalServerError((req, res) -> {
      logger.error("500, internal server error: ");
      res.status(500);
      return res;
    });
  }

  private static void setupApiController() {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // Role
    RoleDao roleDao = new RoleDao(sessionFactory);
    RoleService roleService = new RoleService(roleDao);

    // User
    UserDao userDao = new UserDao(sessionFactory);
    UserService userService = new UserService(userDao, roleService);
    new UserController(userService);
  }
  
  private static void setupKeyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance();
      keyStore.generateKey(KEY_FILE);
    } catch (NoSuchAlgorithmException | IOException e) {
      logger.error("Cannot instantiate keystore: " + e.getMessage());
    }
  }
}
