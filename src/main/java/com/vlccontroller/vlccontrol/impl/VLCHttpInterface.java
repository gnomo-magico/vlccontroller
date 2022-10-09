package com.vlccontroller.vlccontrol.impl;

import com.google.gson.Gson;
import com.vlccontroller.commons.Command;
import com.vlccontroller.dto.VLCInstance;
import com.vlccontroller.exceptions.VLCConnectionException;
import com.vlccontroller.vlccontrol.VLCInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

public final class VLCHttpInterface implements VLCInterface {

  private final String host;
  private final String password;
  private static final String META_INFO_PATH = "/requests/status.json";
  private static final String AUTH_HEADER = "Authorization";
  private static final String AUTH_TYPE = "Basic ";
  private static final String HTTP_METHOD = "GET";

  /**
   * Crea un'istanza per la connessione con un'interfaccia http esistente di vlc.
   *
   * @param host     dell'interfaccia http
   * @param password dell'interfaccia http
   * @throws VLCConnectionException se fallisce nel tentativo di connettersi all'interfaccia
   */
  public VLCHttpInterface(String host, String password) throws VLCConnectionException {
    try {
      HttpURLConnection conn = (HttpURLConnection) new URL(host).openConnection();
      conn.connect();
      this.host = host;
      this.password = password;
      conn.disconnect();
    } catch (IOException ex) {
      throw new VLCConnectionException(
          "Errore di connessione per host:[" + host + "] - CAUSE: " + ex.getMessage());
    }
  }

  /**
   * Metodo per ottenere l'oggetto di risposta dell'interfaccia http di vlc, contenente le
   * informazioni sull'istanza corrente
   *
   * @return l'optional di VLCInstance
   * @throws VLCConnectionException se fallisce nel tentativo di connessione
   */
  @Override
  public Optional<VLCInstance> getInstanceMetaInformation() throws VLCConnectionException {
    return getResponseFromConnection(getVLCHttpConnection(host + META_INFO_PATH));
  }

  private String getAuthParameter() {
    return AUTH_TYPE + Base64.getEncoder().encodeToString((":" + password)
        .getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Mette in pausa l'istanza corrente di vlc
   *
   * @return il messaggio che rappresenta l'esito dell'operazione
   */
  @Override
  public String pauseInstance() {
    Optional<VLCInstance> vlcInstance = getInstanceMetaInformation();
    if (vlcInstance.isPresent()) {
      VLCInstance instance = vlcInstance.get();
      if (instance.isRunning()) {
        HttpURLConnection conn = getVLCHttpConnection(
            host + META_INFO_PATH + "?" + Command.pause());
        if (200 == getResponseCode(conn)) {
          return ":paused:";
        }
      }
    }
    return "The istance is already paused or stopped";
  }

  /**
   * Mette in play l'istanza corrente di vlc
   *
   * @return il messaggio che rappresenta l'esito dell'operazione
   */
  @Override
  public String playInstance() {
    Optional<VLCInstance> vlcInstance = getInstanceMetaInformation();
    if (vlcInstance.isPresent()) {
      VLCInstance instance = vlcInstance.get();
      if (!instance.isRunning()) {
        HttpURLConnection conn = getVLCHttpConnection(
            host + META_INFO_PATH + "?" + Command.play());
        if (200 == getResponseCode(conn)) {
          return ":playing:";
        }
      }
    }
    return "The istance is already playing or is stopped";
  }

  /**
   * Metodo che fa da wrapper al getResponseCode di HttpURLConnection per gestire l'eccezione
   * localmente
   *
   * @param conn la connessione da cui leggere il codice di esito della chiamata
   * @return il codice di risposta o -1 se è stata lanciata un'eccezione
   */
  private int getResponseCode(HttpURLConnection conn) {
    try {
      int code = conn.getResponseCode();
      conn.disconnect();
      return code;
    } catch (IOException e) {
      System.err.println("Errore getResponseCode: " + e.getMessage());
    }
    return -1;
  }

  private HttpURLConnection getVLCHttpConnection(String url) {
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.addRequestProperty(AUTH_HEADER, getAuthParameter());
      connection.setRequestMethod(HTTP_METHOD);
      connection.connect();
      return connection;
    } catch (IOException ex) {
      throw new VLCConnectionException(
          "Errore durante la connessione per url: " + url + "- CAUSE: " + ex.getMessage());
    }
  }

  /**
   * Metodo per ottenere l'oggetto di risposta dalla chiamata http ad un'istanza vlc
   *
   * @param conn connessione http all'istanza vlc
   * @return l'optional di VLCInstance
   */
  private Optional<VLCInstance> getResponseFromConnection(HttpURLConnection conn) {
    try {
      String response = new BufferedReader(new InputStreamReader(conn.getInputStream())).lines()
          .collect(Collectors.joining());
      conn.disconnect();
      return Optional.of(new Gson().fromJson(response, VLCInstance.class));
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
    return Optional.empty();
  }

}
