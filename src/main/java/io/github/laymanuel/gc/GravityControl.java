package io.github.laymanuel.gc;

import org.bukkit.plugin.java.JavaPlugin;

public final class GravityControl extends JavaPlugin {
    public static GravityControlConfig CONFIG;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        CONFIG = new GravityControlConfig(this.getConfig());

        this.getServer().getPluginManager().registerEvents(new GravityListener(), this);
        this.getCommand("gcr").setExecutor(new ReloadCommand(this));
    }
}
