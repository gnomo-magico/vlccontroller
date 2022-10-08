package com.vlccontroller;

import com.vlccontroller.dto.VLCInstance;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Scanner;
import com.vlccontroller.vlccontrol.VLCInterface;
import com.vlccontroller.vlccontrol.impl.VLCHttpInterface;

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
      case "cl":
        clearConsole();
        break;
      default:
        logError("Comando [" + comando + "] non riconosciuto");
        break;
    }
  }

  private static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void exit() {
    logInfo("Bye!");
    System.exit(1);
  }

  public static void getInfo() {
    Optional<VLCInstance> vlcInstance = vlcInterface.getInstanceMetaInformation();
    if (vlcInstance.isPresent()) {
      printVLCInstanceInfo(vlcInstance.get());
    } else {
      logInfo("Nessuna istanza trovata!");
    }
  }

  public static void pause() {
    logInfo(vlcInterface.pauseInstance());
  }

  public static void play() {
    logInfo(vlcInterface.playInstance());
  }

  public static void logError(String message) {
    System.err.println("@[ERROR]:\t" + message);
  }

  public static void logInfo(String message) {
    System.out.println("@[INFO]:\t" + message);
  }

  public static void printVLCInstanceInfo(final VLCInstance instance) {
    logInfo("Info vlc istanza corrente: ");
    System.out.println(instance);
  }

  public static void printUsageAndExit() {
    logError("USAGE: --password=<pass> [--host=<host:port>]\n"
        + "\t\thost     : rappresenta l'host dove è attiva l'istanza vlc (di default http://localhost:8080);\n"
        + "\t\tpassword : password per accedere all'interfaccia http dell'istanza vlc;");
    System.exit(-1);
  }

  public static void parseArguments(String[] args) {
    int len;
    if ((len = args.length) < 1) {
      printUsageAndExit();
    }
    pass = getValueArg(args[0], "--password");
    if (len > 1) {
      host = getValueArg(args[1], "--host");
    } else {
      host = "http://localhost:8080";
    }
  }

  public static String getValueArg(String arg, String keyExpected) {
    String[] tokens = arg.split("=");
    if (tokens.length != 2 || !tokens[0].equals(keyExpected)) {
      printUsageAndExit();
    }
    return tokens[1];
  }


  public static void printWelcomeBanner() {
    try (InputStream in = App.class.getClassLoader().getResourceAsStream("banner.txt")) {
      if (in != null) {
        new BufferedReader(new InputStreamReader(in))
            .lines()
            .forEach(System.out::println);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void printHelp() {
    logInfo("COMANDI: [PL] play | [PA] pause | [IN] info | [CL] clear console | [H] help | [Q] exit :");
  }
}
