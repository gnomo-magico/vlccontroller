package com.vlccontroller.utils;

public final class VLCVolumeParser {

  private static final double VLC_VOLUME = 256.0;

  private VLCVolumeParser() {
  }

  public static String format(double volume) {
    double percentage = (volume / VLC_VOLUME) * 100;
    return (int) percentage + "%";
  }

}
