package ch.hsr.adit.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.PropertyValueException;

public class ExceptionUtil {

  private static volatile ExceptionUtil instance;
  private static final int DEFAULT_STATUS_CODE = 500;
  private static final int UNAUTHORIZED = 401;
  private static final int FORBIDDEN = 403;
  private static final int CONFLICT = 409;
  private static final int NOT_FOUND = 404;


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
    exceptionUtil.exceptionMapping.put(PropertyValueException.class.getSimpleName(), CONFLICT);
    exceptionUtil.exceptionMapping.put(PersistenceException.class.getSimpleName(), CONFLICT);
    exceptionUtil.exceptionMapping.put(IllegalArgumentException.class.getSimpleName(), CONFLICT);
    exceptionUtil.exceptionMapping.put(ObjectNotFoundException.class.getSimpleName(), NOT_FOUND);
    exceptionUtil.exceptionMapping.put(NoResultException.class.getSimpleName(), NOT_FOUND);
    exceptionUtil.exceptionMapping.put(AuthenticationException.class.getSimpleName(), UNAUTHORIZED);
    exceptionUtil.exceptionMapping.put(OptimisticLockException.class.getSimpleName(), CONFLICT);
  }

  public Integer getHttpErrorCode(String exceptionType) {
    Integer statusCode = exceptionMapping.get(exceptionType);
    if (statusCode == null) {
      return DEFAULT_STATUS_CODE;
    } else {
      return statusCode;
    }
  }
  
}
