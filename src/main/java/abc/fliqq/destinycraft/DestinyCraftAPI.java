package abc.fliqq.destinycraft;

import org.bukkit.plugin.java.JavaPlugin;

public class DestinyCraftAPI extends JavaPlugin {
    public void onEnable() {
        getLogger().info("DestinyCraftAPI has been enabled!");
    }

    public void onDisable() {
        getLogger().info("DestinyCraftAPI has been disabled!");
    }
}