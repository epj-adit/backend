package ch.hsr.adit.util;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest {
  
  @Test
  public void formatDateTest() {
    LocalDate localDate = LocalDate.of(2017, 04, 30);
    Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    
    String result = DateUtil.formatDate(date);
    
    assertEquals("30.04.2017", result);
  }

  @Test
  public void parseDateTest() {
    LocalDate localDate = LocalDate.of(2017, 04, 30);
    Date expected = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    String dateString = "30.04.2017";
    
    Date date = DateUtil.parseDate(dateString);
    assertEquals(expected, date);
  }
}
