package one.eim.gravitycontrol;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Blocks blocks = (Blocks) o;
      return sand == blocks.sand && redSand == blocks.redSand && anvil == blocks.anvil && dragonEgg == blocks.dragonEgg && gravel == blocks.gravel && concretePowder == blocks.concretePowder;
    }

    @Override
    public int hashCode() {
      return Objects.hash(sand, redSand, anvil, dragonEgg, gravel, concretePowder);
    }
  }
}
