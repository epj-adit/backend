package ch.hsr.adit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {

  private static final Logger LOGGER = Logger.getLogger(DateUtil.class);
  private static final String DATE_FORMAT = "dd.MM.yyyy";

  private DateUtil() {
    throw new IllegalAccessError("Utility class");
  }
  
  public static String formatDate(Date date) {
    return new SimpleDateFormat(DATE_FORMAT).format(date);
  }

  public static Date parseDate(String dateString) {
    try {
      return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
    } catch (ParseException e) {
      LOGGER.warn("Unable to parse date " + dateString);
      return null;
    }
  }
}
