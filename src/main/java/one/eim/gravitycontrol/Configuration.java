package one.eim.gravitycontrol;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.intellij.lang.annotations.Subst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Configuration {
  private final GravityControl plugin;

  final double horizontalCoefficient;
  final double verticalCoefficient;
  final Set<Key> worlds;
  final Set<Material> blocks;
  final List<String> regions;

  Configuration(GravityControl plugin, FileConfiguration config) {
    this.plugin = plugin;

    if (!config.isSet(Fields._version)) {
      migrate(config);
    }

    // These seem to make it about match vanilla, but where did these values come from??
    // magic number much
    config.addDefault("horizontal-coefficient", 1.46D);
    config.addDefault("vertical-coefficient", -2.4D);
    this.horizontalCoefficient = config.getDouble("horizontal-coefficient");
    this.verticalCoefficient = config.getDouble("vertical-coefficient");

    this.worlds = worlds(config);
    this.blocks = blocks(config);
    this.regions = config.getStringList(Fields.regions);
  }

  private Set<Material> blocks(final ConfigurationSection config) {
    final Set<Material> materials = new HashSet<>();

    for (final String keyString : config.getStringList(Fields.blocks)) {
      final NamespacedKey key = NamespacedKey.fromString(keyString);
      if (key == null) {
        this.plugin.getSLF4JLogger().warn("Invalid key {} supplied in blocks configuration", keyString);
        continue;
      }

      final Material material = Registry.MATERIAL.get(key);
      if (material == null) {
        this.plugin.getSLF4JLogger().warn("Unknown block {} supplied in blocks configuration", key.asString());
      }

      materials.add(material);
    }

    return materials;
  }

  private Set<Key> worlds(final ConfigurationSection config) {
    final Set<Key> keys = new HashSet<>();

    for (final @Subst("minecraft:overworld") String keyString : config.getStringList(Fields.worlds)) {
      if (keyString.equals("*")) {
        this.plugin.getServer().getWorlds().forEach(world -> keys.add(world.key()));
        return keys;
      }

      try {
        keys.add(Key.key(keyString));
      } catch (InvalidKeyException e) {
        this.plugin.getSLF4JLogger().warn("Invalid key {} supplied in worlds configuration", keyString);
      }
    }

    return keys;
  }

  private void migrate(final FileConfiguration config) {
    this.plugin.getSLF4JLogger().info("Performing config migration");

    final List<String> oldWorlds = config.getStringList("worlds");
    final List<String> newWorlds = new ArrayList<>();

    for (final String worldName : oldWorlds) {
      if (worldName.equals("*")) {
        newWorlds.add(worldName);
      } else {
        final World world = this.plugin.getServer().getWorld(worldName);
        if (world == null) {
          this.plugin.getSLF4JLogger().warn("Unable to find world {}, discarding from configuration", worldName);
          continue;
        }

        newWorlds.add(world.key().asString());
      }
    }

    final List<String> newMaterials = new ArrayList<>();

    if (config.getBoolean("enabled-blocks.sand")) {
      newMaterials.add(Material.SAND.key().asString());
    }
    if (config.getBoolean("enabled-blocks.red-sand")) {
      newMaterials.add(Material.RED_SAND.key().asString());
    }
    if (config.getBoolean("enabled-blocks.anvil")) {
      MaterialSetTag.ANVIL.getValues().forEach(material -> newMaterials.add(material.key().asString()));
    }
    if (config.getBoolean("enabled-blocks.dragon-egg")) {
      newMaterials.add(Material.DRAGON_EGG.key().asString());
    }
    if (config.getBoolean("enabled-blocks.gravel")) {
      newMaterials.add(Material.GRAVEL.key().asString());
    }
    if (config.getBoolean("enabled-blocks.concrete-powder")) {
      MaterialTags.CONCRETE_POWDER.getValues().forEach(material -> newMaterials.add(material.key().asString()));
    }

    config.set("enabled-blocks", null);
    config.set(Fields._version, 2);
    config.set(Fields.worlds, newWorlds);
    config.set(Fields.blocks, newMaterials);
    config.set(Fields.regions, List.of("*"));

    config.setComments(Fields._version, List.of(
      "Do not touch. Used for configuration migration."
    ));
    config.setComments(Fields.worlds, List.of(
      "Worlds where gravity duplication is enabled.",
      "`*` means all worlds.",
      "WARNING: World key is used, NOT world name.",
      "For example: `minecraft:overworld` rather than `world`."
    ));
    config.setComments(Fields.blocks, List.of(
      "Blocks for which duping is allowed.",
      "NOTE: Uses Mojang item names, not bukkit Material names."
    ));
    config.setComments(Fields.regions, List.of(
      "List of WorldGuard regions where duping is allowed",
      "`*` means all regions. Checked based on end portal coordinates."
    ));

    try {
      config.save(this.plugin.getDataFolder().toPath().resolve("config.yml").toFile());
    } catch (final IOException e) {
      this.plugin.getSLF4JLogger().warn("Failed to migrate configuration", e);
      this.plugin.getPluginLoader().disablePlugin(this.plugin);
    }
  }

  private static final class Fields {
    static final String _version = "_version";
    static final String worlds = "worlds";
    static final String blocks = "blocks";
    static final String regions = "regions";
  }
}
