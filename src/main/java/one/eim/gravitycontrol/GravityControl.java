package one.eim.gravitycontrol;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class GravityControl extends JavaPlugin {
  private final UpdateChecker updater = new UpdateChecker(this);
  public Configuration config;

  WorldGuardHook worldGuardHook = null;

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    this.config = new Configuration(this, this.getConfig());

    if (this.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
      this.worldGuardHook = new WorldGuardHook(this);
    }

    this.getServer().getPluginManager().registerEvents(new GravityListener(this), this);
    this.getCommand("gcr").setExecutor(new ReloadCommand(this));

    this.getServer().getScheduler().runTaskTimerAsynchronously(this, updater::check, 200, 864000);
    new Metrics(this, 13384);
  }
}
