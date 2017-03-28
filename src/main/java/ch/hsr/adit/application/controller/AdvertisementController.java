package ch.hsr.adit.application.controller;

import static spark.Spark.post;

import java.io.InputStream;

import javax.servlet.MultipartConfigElement;

import ch.hsr.adit.application.service.UserService;

public class AdvertisementController {

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
