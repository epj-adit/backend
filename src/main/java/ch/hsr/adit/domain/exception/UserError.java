package ch.hsr.adit.domain.exception;

public enum UserError implements ErrorCode {
  USER_NOT_FOUND(1001),
  USER_NOT_INSERTED(1002),
  USER_NOT_DELETED(1003),
  USER_NOT_UPDATED(1004),
  USER_CONSTRAINT_VIOLATED(1005);

  private final int errorCode;
  
  private UserError(final int errorCode) {
    this.errorCode = errorCode;
  }
  
  @Override
  public int getErrorCode() {
    return errorCode;
  }
}
