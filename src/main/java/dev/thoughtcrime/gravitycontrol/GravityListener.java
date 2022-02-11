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
import org.bukkit.util.Vector;

import java.util.EnumSet;

import static com.destroystokyo.paper.MaterialSetTag.ANVIL;
import static com.destroystokyo.paper.MaterialTags.CONCRETE_POWDER;

public class GravityListener implements Listener {
  private static final EnumSet<BlockFace> DUPE_ALLOWED_PORTAL_FACES = EnumSet.of(
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
    final Vector velocity = falling.getVelocity().clone();

    for (BlockFace face : DUPE_ALLOWED_PORTAL_FACES) {
      final Block relative = event.getBlock().getRelative(face);
      if (relative.getType() != Material.END_PORTAL) continue;

      if ((face == BlockFace.NORTH && velocity.getZ() < 0D)
        || (face == BlockFace.EAST && velocity.getX() > 0D)
        || (face == BlockFace.SOUTH && velocity.getZ() > 0D)
        || (face == BlockFace.WEST && velocity.getX() < 0D)) {
        this.dupe(falling, velocity);
        return;
      }
    }
  }

  private void dupe(final FallingBlock falling, final Vector velocity) {
    final Location location = falling.getLocation();
    final World world = location.getWorld();
    if (!this.plugin.config.worlds.contains("*") && !this.plugin.config.worlds.contains(world.getName()))
      return;

    final Material m = falling.getBlockData().getMaterial();
    if (
      (m == Material.SAND && !this.plugin.config.blocks.sand)
        || (m == Material.RED_SAND && !this.plugin.config.blocks.redSand)
        || (ANVIL.isTagged(m) && !this.plugin.config.blocks.anvil)
        || (m == Material.DRAGON_EGG && !this.plugin.config.blocks.dragonEgg)
        || (m == Material.GRAVEL && !this.plugin.config.blocks.gravel)
        || (CONCRETE_POWDER.isTagged(m) && !this.plugin.config.blocks.concretePowder)
    ) return;

    world.spawnFallingBlock(falling.getLocation().add(velocity.clone().multiply(0.25)), falling.getBlockData())
      .setVelocity(velocity.clone().multiply(this.plugin.config.horizontalCoefficient).setY(velocity.getY() * this.plugin.config.verticalCoefficient));
  }
}
