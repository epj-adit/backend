package ch.hsr.adit.application.controller;

import static spark.Spark.post;

import java.io.InputStream;

import javax.servlet.MultipartConfigElement;

import ch.hsr.adit.application.service.MediaService;

public class MediaController {

  /**
   * API Controller for /user requests.
   * 
   * @param userService service class
   */
  public MediaController(MediaService mediaService) {

    // upload
    post(RestApi.Media.MEDIA_UPLOAD, (request, response) -> {
      request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
        // Use the input stream to create a file
      }
      return "File uploaded";
    });

  }
}
