package one.eim.gravitycontrol;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Location;

class WorldGuardHook {
  private final GravityControl plugin;

  WorldGuardHook(final GravityControl plugin) {
    this.plugin = plugin;
  }

  WorldGuard wg() {
    return WorldGuard.getInstance();
  }

  boolean dupingAllowed(final Location location) {
    if (this.plugin.config.regions.contains("*")) {
      return true;
    }

    return this.wg().getPlatform().getRegionContainer().createQuery()
      .getApplicableRegions(BukkitAdapter.adapt(location)).getRegions()
      .stream().anyMatch(region -> this.plugin.config.regions.contains(region.getId()));
  }
}
