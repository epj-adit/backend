package ch.hsr.adit.domain.exception;

public enum EntityError implements ErrorCode {
  ENTITY_NOT_FOUND(4004),
  ENTITY_NOT_INSERTED(1002),
  ENTITY_NOT_DELETED(1003),
  ENTITY_NOT_UPDATED(1004);

  private final int errorCode;
  
  private EntityError(final int errorCode) {
    this.errorCode = errorCode;
  }
  
  @Override
  public int getErrorCode() {
    return errorCode;
  }
}
