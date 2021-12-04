package io.github.laymanuel.gc;

public class Version {
  private final boolean outdated;
  private final String latest;

  public Version(boolean outdated, String latest) {
    this.outdated = outdated;
    this.latest = latest;
  }

  public boolean isOutdated() {
    return outdated;
  }

  public String getLatest() {
    return latest;
  }
}
