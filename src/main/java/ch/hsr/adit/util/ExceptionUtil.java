package ch.hsr.adit.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;

public class ExceptionUtil {

  private static volatile ExceptionUtil instance;
  private static final Integer DEFAULT_STATUS_CODE = 200;

  private final Map<String, Integer> exceptionMapping = new HashMap<>();


  private ExceptionUtil() {}

  public static ExceptionUtil getInstance() {
    if (instance == null) {
      synchronized (ExceptionUtil.class) {
        if (instance == null) {
          instance = new ExceptionUtil();
          setupMappings(instance);
        }
      }
    }
    return instance;
  }

  private static void setupMappings(ExceptionUtil exceptionUtil) {
    exceptionUtil.exceptionMapping.put(PropertyValueException.class.getSimpleName(), 404);
    exceptionUtil.exceptionMapping.put(PersistenceException.class.getSimpleName(), 404);
    exceptionUtil.exceptionMapping.put(HibernateException.class.getSimpleName(), 404);
  }

  public Integer getHttpErrorCode(String exceptionType) {
    Integer statusCode = exceptionMapping.get(exceptionType);
    if (statusCode == null) {
      return DEFAULT_STATUS_CODE;
    } else {
      return statusCode;
    }
  }

  // public void test() {
  // if (e instanceof PropertyValueException) {
  // throw new SystemException(DatabaseError.CONSTRAINT_VIOLATED_NULL_FIELD);
  // } else if (e instanceof PersistenceException) {
  // Throwable cause = e.getCause().getCause();
  // if (cause.getMessage().contains("unique constraint")) {
  // throw new SystemException(DatabaseError.CONSTRAINT_VIOLATED_INSERTING_CONFLICT, e);
  // } else {
  // throw new SystemException(DatabaseError.CONSTRAINT_VIOLATED, e);
  // }
  // }

}
