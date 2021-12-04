package io.github.laymanuel.gc;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class UpdateChecker {
  private static final URL endpoint;
  private static final Gson gson = new Gson();

  static {
    URL trypoint;
    try {
      trypoint = new URL("https://gravitycontrol.laymanuel.workers.dev/version");
    } catch (MalformedURLException ignored) {
      trypoint = null;
    }
    endpoint = trypoint;
  }

  private final byte[] version;
  private final int length;
  private final GravityControl plugin;

  public UpdateChecker(GravityControl plugin) {
    this.plugin = plugin;
    this.version = this.plugin.version.getBytes(StandardCharsets.UTF_8);
    this.length = this.version.length;
  }

  public void check() {
    try {
      HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);
      connection.setFixedLengthStreamingMode(length);
      connection.connect();
      connection.getOutputStream().write(version);

      Version response = gson.fromJson(new InputStreamReader(new BufferedInputStream(connection.getInputStream())), Version.class);
      if (response.isOutdated()) {
        this.plugin.getLogger().log(Level.WARNING, "GravityControl is out of date! Current version: " + this.plugin.version + " Latest version: " + response.getLatest() + ".");
        this.plugin.getLogger().log(Level.WARNING, "Please download an updated version of GravityControl from https://github.com/laymanuel/GravityControl/releases");
      }
    } catch (Throwable ignored) {
    }
  }
}
