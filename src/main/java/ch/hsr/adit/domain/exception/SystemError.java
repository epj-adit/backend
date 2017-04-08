package ch.hsr.adit.domain.exception;

public enum SystemError implements ErrorCode {
  JSON_PARSE_ERROR(5001);

  private final int errorCode;
  
  private SystemError(final int errorCode) {
    this.errorCode = errorCode;
  }
  
  @Override
  public int getErrorCode() {
    return errorCode;
  }
}
