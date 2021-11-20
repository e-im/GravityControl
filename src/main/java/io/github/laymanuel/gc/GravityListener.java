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

public class GravityListener implements Listener {
    private static final BlockFace[] DUPE_ALLOWED_PORTAL_FACES = {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

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
                    || (face == BlockFace.WEST && velocity.getX() < 0D)) this.dupe(falling, velocity);

        }
    }

    private void dupe(final FallingBlock falling, Vector velocity) {
        final Location location = falling.getLocation();
        final World world = location.getWorld();
        if (!GravityControl.CONFIG.worlds.contains("*") && !GravityControl.CONFIG.worlds.contains(world.getName()))
            return;

        final Material m = falling.getBlockData().getMaterial();
        if (
                (m == Material.SAND && !GravityControl.CONFIG.blocks.sand)
                        || ((
                        m == Material.ANVIL
                                || m == Material.DAMAGED_ANVIL
                                || m == Material.CHIPPED_ANVIL
                ) && !GravityControl.CONFIG.blocks.anvil)
                        || (m == Material.GRAVEL && !GravityControl.CONFIG.blocks.gravel)
                        || (m.getKey().getKey().contains("concrete_powder") && !GravityControl.CONFIG.blocks.concretePowder)
                        || (m == Material.DRAGON_EGG && !GravityControl.CONFIG.blocks.dragonEgg)
        ) return;

        world.spawnFallingBlock(falling.getLocation().add(velocity.clone().multiply(0.25)), falling.getBlockData())
                .setVelocity(velocity.clone().multiply(GravityControl.CONFIG.horizontalCoefficient).setY(velocity.getY() * GravityControl.CONFIG.verticalCoefficient));
    }
}
