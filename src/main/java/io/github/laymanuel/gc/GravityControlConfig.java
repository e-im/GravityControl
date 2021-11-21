package io.github.laymanuel.gc;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class GravityControlConfig {
  public final double horizontalCoefficient;
  public final double verticalCoefficient;
  public final List<String> worlds;
  public final Blocks blocks;

  public GravityControlConfig(ConfigurationSection config) {
    this.worlds = config.getStringList("worlds");
    this.blocks = new Blocks(config.getConfigurationSection("enabled-blocks"));
    config.addDefault("horizontal-coefficient", 1.46D);
    config.addDefault("vertical-coefficient", -2.4D);
    this.horizontalCoefficient = config.getDouble("horizontal-coefficient");
    this.verticalCoefficient = config.getDouble("vertical-coefficient");
  }

  public static class Blocks {
    public final boolean sand;
    public final boolean redSand;
    public final boolean anvil;
    public final boolean dragonEgg;
    public final boolean gravel;
    public final boolean concretePowder;

    public Blocks(ConfigurationSection config) {
      if (config == null) {
        this.sand = true;
        this.redSand = true;
        this.anvil = true;
        this.dragonEgg = true;
        this.gravel = true;
        this.concretePowder = true;
      } else {
        this.sand = config.getBoolean("sand");
        this.redSand = config.getBoolean("red-sand");
        this.anvil = config.getBoolean("anvil");
        this.dragonEgg = config.getBoolean("dragon-egg");
        this.gravel = config.getBoolean("gravel");
        this.concretePowder = config.getBoolean("concrete-powder");
      }
    }
  }
}
