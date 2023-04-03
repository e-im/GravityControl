package one.eim.gravitycontrol;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;

class UpdateChecker {
  private final HttpClient httpClient;
  private final HttpRequest request;
  private final Gson gson;
  private final GravityControl plugin;

  UpdateChecker(GravityControl plugin) {
    this.httpClient = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.ALWAYS)
      .connectTimeout(Duration.ofSeconds(2))
      .build();
    this.request = HttpRequest.newBuilder(URI.create("https://gravitycontrol.eim.one/version"))
      .POST(HttpRequest.BodyPublishers.ofString(plugin.getDescription().getVersion()))
      .build();
    this.gson = new Gson();
    this.plugin = plugin;
  }

  void check() {
    Compatibility.runTaskAsync(this.plugin, () -> {
      final Version version;
      try {
        version = this.gson.fromJson(
          this.httpClient.send(this.request, HttpResponse.BodyHandlers.ofString()).body(),
          Version.class
        );
      } catch (IOException | InterruptedException e) {
        this.plugin.getSLF4JLogger().info("Failed to check for updates", e);
        return;
      }

      if (version.outdated()) {
        this.plugin.getLogger().log(Level.INFO, "GravityControl is out of date! Current version: " + this.plugin.getDescription().getVersion() + " Latest version: " + version.latest() + ".");
        this.plugin.getLogger().log(Level.INFO, "Please download an updated version of GravityControl from https://dev.bukkit.org/projects/gravity-control");
      }
    });
  }

  private static final class Version {
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
