package ch.hsr.adit.exception;

public class DatabaseException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  private static final int ERROR_CODE = 1001;

  public DatabaseException () {
  }

  public DatabaseException (String message) {
      super (message);
  }
  
  public DatabaseException(String message, Object... args) {
    super(String.format(message, args));
  }

  public DatabaseException (Throwable cause) {
      super (cause);
  }

  public DatabaseException (String message, Throwable cause) {
      super (message, cause);
  }
  
  public int getErrorCode() {
    return ERROR_CODE;
  }

}
