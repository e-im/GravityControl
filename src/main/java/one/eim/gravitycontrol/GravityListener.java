package one.eim.gravitycontrol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.EnumSet;

public class GravityListener implements Listener {
  private static final EnumSet<BlockFace> BLOCK_FACES = EnumSet.of(
    BlockFace.NORTH,
    BlockFace.EAST,
    BlockFace.SOUTH,
    BlockFace.WEST
  );

  private final GravityControl plugin;

  public GravityListener(final GravityControl plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
    if (event.getTo() == Material.AIR || !(event.getEntity() instanceof final FallingBlock falling)) return;
    final BoundingBox boundingBox = falling.getBoundingBox().expand(-0.01D);

    for (BlockFace face : BLOCK_FACES) {
      final Block relative = event.getBlock().getRelative(face);
      if (relative.getType() != Material.END_PORTAL) continue;

      if (relative.getBoundingBox().overlaps(boundingBox)) {
        this.dupe(falling, face.getDirection());
      }
    }
  }

  private void dupe(final FallingBlock falling, final Vector portalRelativeVector) {
    final Location location = falling.getLocation();
    final World world = location.getWorld();
    if (!(this.plugin.config.worlds.contains(world.key()) || this.plugin.config.blocks.contains(falling.getBlockData().getMaterial())))
      return;

    final Vector vector = falling.getVelocity();

    if (vector.getX() == 0 && vector.getZ() == 0) {
      world.spawnFallingBlock(falling.getLocation().add(portalRelativeVector.multiply(0.25D).setY(0.05D)), falling.getBlockData());
      return;
    }

    world.spawnFallingBlock(location.add(vector.clone().multiply(0.25)), falling.getBlockData())
      .setVelocity(vector.clone().multiply(this.plugin.config.horizontalCoefficient).setY(vector.getY() * this.plugin.config.verticalCoefficient));
  }
}
