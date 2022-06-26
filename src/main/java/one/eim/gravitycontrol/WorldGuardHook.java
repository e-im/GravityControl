package one.eim.gravitycontrol;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import org.bukkit.Location;

class WorldGuardHook {
  private static final String FLAG_NAME = "gravity-dupe";
  private static final StateFlag FLAG = new StateFlag(FLAG_NAME, true);
  private final GravityControl plugin;

  WorldGuardHook(final GravityControl plugin) {
    this.plugin = plugin;
  }

  WorldGuard wg() {
    return WorldGuard.getInstance();
  }

  boolean register() {
    try {
      this.wg().getFlagRegistry().register(FLAG);
    } catch (final FlagConflictException e) {
      plugin.getSLF4JLogger().warn("Failed to enable WorldGuard hook. Flag with name {} already exists!", FLAG_NAME, e);
      return false;
    }

    plugin.getSLF4JLogger().info("Enabled WorldGuard hook");
    return true;
  }

  State dupingAllowed(final Location location) {
    return this.wg().getPlatform().getRegionContainer().createQuery()
      .getApplicableRegions(BukkitAdapter.adapt(location))
      .queryState(null, FLAG);
  }
}
