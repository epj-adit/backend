package ch.hsr.adit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class DateUtil {

  private static final Logger LOGGER = Logger.getLogger(DateUtil.class);
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

  public static String formatDate(Date date) {
    return DATE_FORMAT.format(date);
  }

  public static Date parseDate(String dateString) {
    try {
      return DATE_FORMAT.parse(dateString);
    } catch (ParseException e) {
      LOGGER.warn("Unable to parse date " + dateString);
      return null;
    }
  }
}
