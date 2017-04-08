package ch.hsr.adit.domain.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SystemException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int errorCode;
  private final Exception exception;

  public SystemException(ErrorCode errorCode) {
    this.errorCode = errorCode.getErrorCode();
    this.exception = null;
  }

  public SystemException(ErrorCode errorCode, Exception exception) {
    if (exception instanceof SystemException) {
      this.errorCode = ((SystemException) exception).getErrorCode();
      this.exception = null;
    } else {
      this.errorCode = errorCode.getErrorCode();
      this.exception = exception;
    }
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public Exception getException() {
    return this.exception;
  }

  @Override
  public String getMessage() {
    if (exception != null) {
      return errorCode + " :" + exception.getMessage();
    }
    return String.valueOf(errorCode);
  }

}
