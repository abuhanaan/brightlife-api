package com.fronteers.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

  public static String getIsoTime(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    return timestamp.toLocalDateTime().format(formatter);
  }

  public static Timestamp nowByTimeZoneLagos() {
    TimeZone timeZone1 = TimeZone.getTimeZone("Africa/Lagos");
    return Timestamp.valueOf(ZonedDateTime.now(timeZone1.toZoneId()).toLocalDateTime());
  }

  public static Timestamp now() {
    Date date = new Date();
    return new Timestamp(date.getTime());
  }

  public static Timestamp futureTime(int seconds) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.SECOND, seconds);
    Date expiryDate = cal.getTime();
    return new Timestamp(expiryDate.getTime());
  }

  public static Timestamp futureTimeZone(int seconds) {
    ZoneId zone = ZoneId.of("Africa/Lagos");
    ZonedDateTime zdt = ZonedDateTime.now(zone);
    Calendar cal = GregorianCalendar.from(zdt);
    cal.setTime(new Date());
    cal.add(Calendar.SECOND, seconds);
    Date expiryDate = cal.getTime();
    return new Timestamp(expiryDate.getTime());
  }

  public static Integer timestampToUnixSeconds() {
    Date date = new Date();
    return (int) (date.getTime() / 1000);
  }

  public static String getGMTTime() {
    final Date currentTime = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sdf.format(currentTime);
  }

  public static Date convertStringToDate(String inDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh24:mm:ss");
    Date date = new Date();
    try {
      date = sdf.parse(inDate);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static Timestamp getDateByTimeZoneDateWithSpecificNumberOfDays(int numberOfDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.setTimeZone(TimeZone.getTimeZone("Africa/Lagos"));
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    calendar.add(Calendar.DATE, numberOfDays);
    Date startDate = calendar.getTime();
    return new Timestamp(startDate.getTime());
  }

}
