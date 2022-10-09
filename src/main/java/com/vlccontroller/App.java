package com.vlccontroller;

import com.vlccontroller.cli.ArgumentBuilder;
import com.vlccontroller.cli.ArgumentsParser;
import com.vlccontroller.dto.VLCInstance;
import com.vlccontroller.exceptions.ArgumentException;
import com.vlccontroller.vlccontrol.VLCInterface;
import com.vlccontroller.vlccontrol.impl.VLCHttpInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Scanner;

@SuppressWarnings("all")
public class App {

  private static String host;
  private static String pass;
  private static VLCInterface vlcInterface;

  public static void main(String[] args) {
    parseArguments(args);
    vlcInterface = new VLCHttpInterface(host, pass);
    printWelcomeBanner();
    logInfo("Connessione con host " + host + " avvenuta correttamente_");
    printHelp();
    Scanner sc;
    String comando;
    for (; ; ) {
      sc = new Scanner(System.in);
      comando = sc.nextLine().toLowerCase();
      eseguiComando(comando);
    }
  }

  private static void eseguiComando(String comando) {
    switch (comando) {
      case "pl":
        play();
        break;
      case "pa":
        pause();
        break;
      case "in":
        getInfo();
        break;
      case "q":
        exit();
      case "h":
        printHelp();
        break;
      default:
        logError("Comando [" + comando + "] non riconosciuto");
        break;
    }
  }

  private static void exit() {
    logInfo("Bye!");
    System.exit(1);
  }

  private static void getInfo() {
    Optional<VLCInstance> vlcInstance = vlcInterface.getInstanceMetaInformation();
    if (vlcInstance.isPresent()) {
      printVLCInstanceInfo(vlcInstance.get());
    } else {
      logInfo("Nessuna istanza trovata!");
    }
  }

  private static void pause() {
    logInfo(vlcInterface.pauseInstance());
  }

  private static void play() {
    logInfo(vlcInterface.playInstance());
  }

  private static void logError(String message) {
    System.err.println("@[ERROR]:\t" + message);
  }

  private static void logInfo(String message) {
    System.out.println("@[INFO]:\t" + message);
  }

  private static void printVLCInstanceInfo(final VLCInstance instance) {
    logInfo("Info vlc istanza corrente: ");
    System.out.println(instance);
  }

  private static void printUsageAndExit() {
    logError("USAGE: --password=<pass> [--host=<host:port>]\n"
        + "\t\thost     : rappresenta l'host dove è attiva l'istanza vlc (di default http://localhost:8080);\n"
        + "\t\tpassword : password per accedere all'interfaccia http dell'istanza vlc;");
    System.exit(-1);
  }

  private static void parseArguments(String[] args) {
    ArgumentsParser parser = ArgumentsParser.createArgs(
            ArgumentBuilder
                .of("password")
                .build(),
            ArgumentBuilder
                .of("host")
                .optional("http://localhost:8080")
                .build())
        .parse(args);
    try {
      pass = parser.get("password");
      host = parser.get("host");
    } catch (ArgumentException ex) {
      logError(ex.getMessage());
      printUsageAndExit();
    }

  }

  private static void printWelcomeBanner() {
    try (InputStream in = App.class.getClassLoader().getResourceAsStream("banner.txt")) {
      if (in != null) {
        new BufferedReader(new InputStreamReader(in))
            .lines()
            .forEach(System.out::println);
      }
    } catch (IOException ignore) {
    }
  }

  private static void printHelp() {
    logInfo("COMANDI: [PL] play | [PA] pause | [IN] info |  [H] help | [Q] exit :");
  }
}
