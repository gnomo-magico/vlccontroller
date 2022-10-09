package com.vlccontroller.cli;

public class Argument {

  private boolean required = true;
  private String defaultValue;
  private final String name;
  private String value;

  Argument(String name) {
    this.name = name;
  }

  Argument(String name, String defaultValue) {
    this(name);
    if (defaultValue != null) {
      this.defaultValue = defaultValue;
      this.required = false;
    }
  }

  public boolean isRequired() {
    return this.required;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  protected void setValue(String value) {
    this.value = value;
  }
}
