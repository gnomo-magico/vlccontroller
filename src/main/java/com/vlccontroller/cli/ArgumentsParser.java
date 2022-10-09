package com.vlccontroller.cli;

import exception.ArgumentException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArgumentsParser {


  private final Map<String, Argument> argMap;

  private ArgumentsParser(List<Argument> arguments) {
    this.argMap = arguments.stream().collect(Collectors.toMap(Argument::getName, a -> a));
  }

  public static ArgumentsParser createArgs(Argument... arguments) {
    return new ArgumentsParser(Arrays.asList(arguments));
  }

  public ArgumentsParser parse(String[] args) {
    List<String> cliLine = Arrays.asList(args);
    argMap.forEach((key, value) -> {
      cliLine.stream()
          .filter(s -> s.contains(key))
          .findFirst()
          .ifPresent(s -> setValueArg(s, value));
    });
    return this;
  }

  private void setValueArg(String token, Argument arg) {
    String[] tokenSplitted = token.split("=");
    if (tokenSplitted.length != 2) {
      throw new ArgumentException("Malformed argument value: " + arg.getName());
    }
    arg.setValue(tokenSplitted[1]);
  }

  public String get(String argName) {
    Argument arg = argMap.get(argName);
    String value;
    if (arg == null) {
      return null;
    }
    if (arg.isRequired()) {
      if ((value = arg.getValue()) == null) {
        throw new ArgumentException("Missing required Argument: " + argName);
      }
      return value;
    } else {
      return (value = arg.getValue()) == null ? arg.getDefaultValue() : value;
    }
  }

}
