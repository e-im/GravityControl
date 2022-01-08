package dev.thoughtcrime.gravitycontrol;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;

public class UpdateChecker {
  private final HttpClient httpClient;
  private final HttpRequest request;
  private final Gson gson;
  private final GravityControl plugin;

  public UpdateChecker(GravityControl plugin) {
    this.httpClient = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.ALWAYS)
      .connectTimeout(Duration.ofSeconds(2))
      .build();

    this.request = HttpRequest.newBuilder(URI.create("https://gravitycontrol.thoughtcrime.dev/version"))
      .POST(HttpRequest.BodyPublishers.ofString(plugin.getDescription().getVersion()))
      .build();

    this.gson = new Gson();

    this.plugin = plugin;
  }

  public void check() {
    try {
      Version version = this.gson.fromJson(
        this.httpClient.send(this.request, HttpResponse.BodyHandlers.ofString()).body(),
        Version.class
      );

      if (version.outdated()) {
        this.plugin.getLogger().log(Level.INFO, "GravityControl is out of date! Current version: " + this.plugin.getDescription().getVersion() + " Latest version: " + version.latest() + ".");
        this.plugin.getLogger().log(Level.INFO, "Please download an updated version of GravityControl from https://github.com/laymanuel/GravityControl/releases");
      }
    } catch (Throwable ignored) {
    }
  }

  public static final class Version {
    private final boolean outdated;
    private final String latest;

    public Version(final boolean outdated, final String latest) {
      this.outdated = outdated;
      this.latest = latest;
    }

    public boolean outdated() {
      return outdated;
    }

    public String latest() {
      return latest;
    }
  }
}
