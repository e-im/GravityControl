package io.github.laymanuel.gc;

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

public class GravityListener implements Listener {
  private static final EnumSet<BlockFace> DUPE_ALLOWED_PORTAL_FACES = EnumSet.of(
    BlockFace.NORTH,
    BlockFace.EAST,
    BlockFace.SOUTH,
    BlockFace.WEST
  );
  private static final EnumSet<Material> CONCRETE_POWDERS = EnumSet.of(
    Material.WHITE_CONCRETE_POWDER,
    Material.ORANGE_CONCRETE_POWDER,
    Material.MAGENTA_CONCRETE_POWDER,
    Material.LIGHT_BLUE_CONCRETE_POWDER,
    Material.YELLOW_CONCRETE_POWDER,
    Material.LIME_CONCRETE_POWDER,
    Material.PINK_CONCRETE_POWDER,
    Material.GRAY_CONCRETE_POWDER,
    Material.LIGHT_GRAY_CONCRETE_POWDER,
    Material.CYAN_CONCRETE_POWDER,
    Material.PURPLE_CONCRETE_POWDER,
    Material.BLUE_CONCRETE_POWDER,
    Material.BROWN_CONCRETE_POWDER,
    Material.GREEN_CONCRETE_POWDER,
    Material.RED_CONCRETE_POWDER,
    Material.BLACK_CONCRETE_POWDER
  );
  private static final EnumSet<Material> ANVILS = EnumSet.of(
    Material.ANVIL,
    Material.CHIPPED_ANVIL,
    Material.DAMAGED_ANVIL
  );

  private final GravityControl plugin;

  public GravityListener(final GravityControl plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
    if (!(event.getEntity() instanceof FallingBlock) || event.getTo() == Material.AIR) return;
    final FallingBlock falling = (FallingBlock) event.getEntity();
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
        || (ANVILS.contains(m) && !this.plugin.config.blocks.anvil)
        || (m == Material.DRAGON_EGG && !this.plugin.config.blocks.dragonEgg)
        || (m == Material.GRAVEL && !this.plugin.config.blocks.gravel)
        || (CONCRETE_POWDERS.contains(m) && !this.plugin.config.blocks.concretePowder)
    ) return;

    world.spawnFallingBlock(falling.getLocation().add(velocity.clone().multiply(0.25)), falling.getBlockData())
      .setVelocity(velocity.clone().multiply(this.plugin.config.horizontalCoefficient).setY(velocity.getY() * this.plugin.config.verticalCoefficient));
  }
}
