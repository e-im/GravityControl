package dev.thoughtcrime.gravitycontrol;

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

import static com.destroystokyo.paper.MaterialSetTag.ANVIL;
import static com.destroystokyo.paper.MaterialTags.CONCRETE_POWDER;

public class GravityListener implements Listener {
  private static final Vector ZERO = new Vector(0, 0, 0);
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
    if (!(event.getEntity() instanceof final FallingBlock falling) || event.getTo() == Material.AIR) return;
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
    if (!this.plugin.config.worlds.contains("*") && !this.plugin.config.worlds.contains(world.getName()))
      return;

    final Material m = falling.getBlockData().getMaterial();
    if ((m == Material.SAND && !this.plugin.config.blocks.sand)
        || (m == Material.RED_SAND && !this.plugin.config.blocks.redSand)
        || (ANVIL.isTagged(m) && !this.plugin.config.blocks.anvil)
        || (m == Material.DRAGON_EGG && !this.plugin.config.blocks.dragonEgg)
        || (m == Material.GRAVEL && !this.plugin.config.blocks.gravel)
        || (CONCRETE_POWDER.isTagged(m) && !this.plugin.config.blocks.concretePowder)
    ) return;

    final Vector velocity = falling.getVelocity();

    if (velocity.equals(ZERO)) {
      world.spawnFallingBlock(falling.getLocation().add(portalRelativeVector.multiply(0.25D).setY(0.05D)), falling.getBlockData());
      return;
    }

    world.spawnFallingBlock(location.add(velocity.clone().multiply(0.25)), falling.getBlockData())
      .setVelocity(velocity.clone().multiply(this.plugin.config.horizontalCoefficient).setY(velocity.getY() * this.plugin.config.verticalCoefficient));
  }
}
