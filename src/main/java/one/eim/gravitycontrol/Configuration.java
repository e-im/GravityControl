package one.eim.gravitycontrol;

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.intellij.lang.annotations.Subst;

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

  Configuration(GravityControl plugin, ConfigurationSection config) {
    this.plugin = plugin;

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

  private static final class Fields {
    static final String worlds = "worlds";
    static final String blocks = "blocks";
    static final String regions = "regions";
  }
}
