package ch.hsr.adit.app;

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

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import ch.hsr.adit.business.controller.UserController;
import ch.hsr.adit.business.service.UserService;
import ch.hsr.adit.exception.ErrorResponse;
import ch.hsr.adit.model.User;
import ch.hsr.adit.persistence.GenericDao;
import ch.hsr.adit.persistence.UserDao;
import ch.hsr.adit.util.AuthenticationUtil;
import ch.hsr.adit.util.HibernateUtil;
import ch.hsr.adit.util.KeyStore;

public class App {

  private static final Logger logger = Logger.getLogger(App.class);
  

  public static void main(String[] args) {

    setupApiController();

    setupSeverHandler();

    // wait for jetty
    awaitInitialization();
    
    //TODO: init secret
    // try {
    // KeyStore.generateKey();
    // KeyStore.saveKey();
    // } catch (NoSuchAlgorithmException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

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
      //TODO: send jwt on successfull auth
//    response.header("Authorization", "Bearer " + jwt");
    });

    exception(Exception.class, (e, req, res) -> {
      logger.error("Unexpected exception occured: " + e.getMessage());

      res.status(200);
      res.body(toJson(new ErrorResponse(e)));
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

    // User
    GenericDao<User, Long> userDao = new UserDao(sessionFactory);
    UserService userService = new UserService(userDao);
    new UserController(userService);
  }
}
