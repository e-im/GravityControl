package io.github.laymanuel.gc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
  private final GravityControl plugin;

  public ReloadCommand(GravityControl plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "GravityControl" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + "Attempting to reload configuration...");
    this.plugin.reloadConfig();
    this.plugin.config = new GravityControlConfig(this.plugin.getConfig());
    sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "GravityControl" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + "Successfully reloaded configuration!");
    return true;
  }
}
