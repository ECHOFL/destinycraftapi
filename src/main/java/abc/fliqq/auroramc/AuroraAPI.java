package abc.fliqq.auroramc;

import org.bukkit.plugin.java.JavaPlugin;

public class AuroraAPI extends JavaPlugin {



    @Override
    public void onEnable() {
        getLogger().info("AuroraAPI has been enabled!");
    }
    @Override
    public void onDisable() {
        getLogger().info("AuroraAPI has been disabled!");
    }
}