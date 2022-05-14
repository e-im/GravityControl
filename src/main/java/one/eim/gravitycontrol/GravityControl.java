package one.eim.gravitycontrol;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class GravityControl extends JavaPlugin {
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

    if (this.config.updateChecker) new UpdateChecker(this).check();
    new Metrics(this, 13384);
  }
}
