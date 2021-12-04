package io.github.laymanuel.gc;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;


public final class GravityControl extends JavaPlugin {
  public static final int PLUGIN_ID = 13384;
  public final String version = "@VERSION@";
  public GravityControlConfig config;
  private final UpdateChecker updater = new UpdateChecker(this);

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    this.config = new GravityControlConfig(this.getConfig());

    this.getServer().getPluginManager().registerEvents(new GravityListener(this), this);
    this.getCommand("gcr").setExecutor(new ReloadCommand(this));

    Metrics metrics = new Metrics(this, PLUGIN_ID);
    metrics.addCustomChart(new SimplePie("used_world_configuration", () -> config.worlds.contains("*") && config.worlds.size() == 1 ? "No" : "Yes"));
    metrics.addCustomChart(new SimplePie("used_block_configuration", () -> config.blocks.equals(new GravityControlConfig.Blocks(null)) ? "No" : "Yes"));

    this.getServer().getScheduler().runTaskTimerAsynchronously(this, updater::check, 200, 864000);
  }
}
