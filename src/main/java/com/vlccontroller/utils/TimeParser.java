package com.vlccontroller.utils;

public final class TimeParser {

  private TimeParser() {
  }

  public static String getParsedTime(final long time) {
    long seconds = time % 60;
    long minutes = (time - seconds) / 60;
    return format(minutes) + ":" + format(seconds);
  }

  private static String format(final long time) {
    return time < 10 ? "0" + time : "" + time;
  }
}
