package ch.hsr.adit.domain.model.exception;

public class ForbiddenException extends RuntimeException {
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8527588163796311569L;

  public ForbiddenException(String message) {
    super(message);
  }
}
