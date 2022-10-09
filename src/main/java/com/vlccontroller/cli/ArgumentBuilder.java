package com.vlccontroller.cli;

public class ArgumentBuilder {

  private boolean required = true;
  private final String name;
  private String defaultValue;

  private ArgumentBuilder(String name) {
    this.name = name;
  }

  public static ArgumentBuilder of(String name) {
    return new ArgumentBuilder(name);
  }

  public ArgumentBuilder optional(String defaultValue) {
    this.required = false;
    this.defaultValue = defaultValue;
    return this;
  }


  public Argument build() {
    return required ? new Argument(name) : new Argument(name, defaultValue);
  }

}
