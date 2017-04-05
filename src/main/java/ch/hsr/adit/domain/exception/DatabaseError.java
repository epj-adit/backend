package ch.hsr.adit.domain.exception;

public enum DatabaseError implements ErrorCode {
  USER_CONSTRAINT_VIOLATED(1005);

  private final int errorCode;
  
  private DatabaseError(final int errorCode) {
    this.errorCode = errorCode;
  }
  
  @Override
  public int getErrorCode() {
    return errorCode;
  }
}
