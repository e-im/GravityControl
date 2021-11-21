package io.github.laymanuel.gc;

import org.bukkit.plugin.java.JavaPlugin;

public final class GravityControl extends JavaPlugin {
  public GravityControlConfig config;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    this.config = new GravityControlConfig(this.getConfig());

    this.getServer().getPluginManager().registerEvents(new GravityListener(this), this);
    this.getCommand("gcr").setExecutor(new ReloadCommand(this));
  }
}
