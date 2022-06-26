package one.eim.gravitycontrol;

import com.sk89q.worldguard.protection.flags.StateFlag.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Set;

public class GravityListener implements Listener {
  private static final Set<Vector> DIRECTIONS = Set.of(
    new Vector(0, 0, -1),
    new Vector(1, 0, 0),
    new Vector(0, 0, 1),
    new Vector(-1, 0, 0)
  );

  private final GravityControl plugin;

  public GravityListener(final GravityControl plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
    if (event.getTo() == Material.AIR
      || !(event.getEntity() instanceof final FallingBlock falling)
      || !this.plugin.config.worlds.contains(event.getBlock().getWorld().key())
      || !this.plugin.config.blocks.contains(falling.getBlockData().getMaterial())
    ) {
      return;
    }

    final BoundingBox boundingBox = falling.getBoundingBox().expand(-0.01D);

    for (Vector direction : DIRECTIONS) {
      final Location loc = event.getBlock().getLocation().add(direction);
      if (!loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
        continue;
      }

      final Block block = loc.getBlock();
      if (block.getType() != Material.END_PORTAL || !block.getBoundingBox().overlaps(boundingBox)) {
        continue;
      }

      // Check end portal position. This seems to make more sense than checking block or entity location
      // as this can vary by design
      if (this.plugin.worldGuardHook != null && this.plugin.worldGuardHook.dupingAllowed(loc) != State.ALLOW) {
        continue;
      }

      final Vector velocity = falling.getVelocity();

      if (velocity.getX() == 0 && velocity.getZ() == 0) {
        loc.getWorld().spawnFallingBlock(falling.getLocation().add(direction.getX() * 0.25D, 0.05D, direction.getZ() * 0.25D), falling.getBlockData());
      } else {
        final Vector endVelocity = velocity
          .setX(velocity.getX() * this.plugin.config.horizontalCoefficient)
          .setY(velocity.getY() * this.plugin.config.verticalCoefficient)
          .setZ(velocity.getZ() * this.plugin.config.horizontalCoefficient);

        falling.getWorld().spawnFallingBlock(
          falling.getLocation().add(direction.getX() * 0.25D, direction.getY() * 0.25D, direction.getZ() * 0.25D),
          falling.getBlockData()
        ).setVelocity(endVelocity);
      }
    }
  }
}
