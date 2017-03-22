package ch.hsr.adit.business.controller;

import static spark.Spark.post;

import java.io.InputStream;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.Logger;

import ch.hsr.adit.business.service.UserService;

public class AdvertisementController {

  private static final Logger logger = Logger.getLogger(AdvertisementController.class);

  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public AdvertisementController(UserService userService) {

    // upload
    post("/advertisement/uploadProductImage", (request, response) -> {
      request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
        // Use the input stream to create a file
      }
      return "File uploaded";
    });

  }
}
