package one.eim.gravitycontrol;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
  private static final Component prefix = Component.text("[", NamedTextColor.GRAY)
    .append(Component.text("GravityControl", NamedTextColor.DARK_GRAY))
    .append(Component.text("]", NamedTextColor.GRAY))
    .append(Component.space());
  private final GravityControl plugin;

  public ReloadCommand(GravityControl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    sender.sendMessage(prefix.append(Component.text("Attempting to reload configuration...", NamedTextColor.WHITE)));
    this.plugin.reloadConfig();
    this.plugin.config = new GravityControlConfig(this.plugin.getConfig());
    sender.sendMessage(prefix.append(Component.text("Successfully reloaded configuration!", NamedTextColor.GREEN)));
    return true;
  }
}
