package ch.hsr.adit.domain.model;

public class ForbiddenException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ForbiddenException(String ex) {
    super(ex);
  }

  public String getSimpleName() {
    return "ForbiddenException";
  }
}
