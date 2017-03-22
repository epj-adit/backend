package ch.hsr.adit.exception;

public class ErrorResponse {
  private String message;

  public ErrorResponse(String message, Object... args) {
    this.message = String.format(message, args);
  }

  public ErrorResponse(Exception ex) {
    this.message = ex.getMessage();
  }

  public String getMessage() {
    return this.message;
  }
}
