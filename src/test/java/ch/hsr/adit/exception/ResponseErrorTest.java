package ch.hsr.adit.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResponseErrorTest {

  @Test
  public void testResponseErrorArguments() {
    String message = "Exception occured with param %s";

    ErrorResponse error = new ErrorResponse(message, "test");
    
    String expectedMessage = "Exception occured with param test";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
  public void testResponseErrorException() {
    String expectedMessage = "Exception occured";
    Exception exception = new Exception(expectedMessage);
    ErrorResponse error = new ErrorResponse(exception);

    assertEquals(expectedMessage, error.getMessage());
  }
}
