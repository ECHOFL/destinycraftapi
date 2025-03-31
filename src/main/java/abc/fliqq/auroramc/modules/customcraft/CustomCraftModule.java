package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.configuration.file.FileConfiguration;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.PluginModule;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.modules.customcraft.listener.MenuListener;
import abc.fliqq.auroramc.modules.customcraft.listener.VillagerListener;
import lombok.Getter;

public class CustomCraftModule implements PluginModule {

    @Getter private final AuroraAPI plugin;
    @Getter private CustomCraftManager customCraftManager;
    private FileConfiguration customCraftConfig;

    public CustomCraftModule(AuroraAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "CustomCraft";
    }

    @Override
    public void onInit() {
        customCraftConfig = plugin.getConfigManager().createModuleConfiguration(this, "config.yml");
        plugin.getModuleManager().registerModule(this);

        // Register the custom crafting recipes
        customCraftManager = new CustomCraftManager(this);

        // register listener
        plugin.getServer().getPluginManager().registerEvents(new VillagerListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
        // register commands
        plugin.getCommand("customcraft").setExecutor(new CustomCraftCommand(this));

        LoggerUtil.info(getName() + " module initialisé.");

    }

    @Override
    public void onStop() {
        LoggerUtil.info(getName() + " module désactivé.");

    }

    @Override
    public void onReload() {
        // Reload the configuration
        customCraftConfig = plugin.getConfigManager().getConfig("modules/customcraft/config.yml");
        LoggerUtil.info(getName() + " module rechargé.");
    }
    
}
