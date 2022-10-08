package com.vlccontroller.utils;

import com.vlccontroller.exceptions.VLCConnectionException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class URLParser {

  private URLParser() {
  }

  public static String urlEncoded(final String name, final String value) {
    try {
      return String.format(name + "=%s",
          URLEncoder.encode(value, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new VLCConnectionException("Errore di encoding: " + e.getMessage());
    }

  }

}
