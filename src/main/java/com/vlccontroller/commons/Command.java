package com.vlccontroller.commons;

import com.vlccontroller.utils.URLParser;

public final class Command {

  private Command() {
  }

  private static final String PAUSE = "pl_pause";
  private static final String PLAY = "pl_play";
  private static final String COMMAND = "command";

  public static String play() {
    return urlEncoded(PLAY);
  }

  public static String pause() {
    return urlEncoded(PAUSE);
  }

  private static String urlEncoded(final String command) {
    return URLParser.urlEncoded(COMMAND, command);
  }
}
