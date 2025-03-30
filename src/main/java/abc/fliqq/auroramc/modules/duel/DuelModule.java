package abc.fliqq.auroramc.modules.duel;

import org.bukkit.configuration.file.FileConfiguration;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.PluginModule;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import lombok.Getter;

public class DuelModule implements PluginModule {
    @Getter
    private final AuroraAPI plugin;
    private FileConfiguration duelConfig;


    public DuelModule(AuroraAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Duel";
    }
    @Override
    public void onInit() {
        duelConfig = plugin.getConfigManager().createModuleConfiguration(this, "config.yml");
        plugin.getModuleManager().registerModule(this);

        LoggerUtil.info(getName() + " module initialisé.");
    }

    @Override
    public void onStop() {
        LoggerUtil.info(getName() + " module désactivé.");
    }

    @Override
    public void onReload() {
        // Reload the configuration
        duelConfig = plugin.getConfigManager().getConfig("modules/duel/config.yml");
        LoggerUtil.info(getName() + " module rechargé.");
    }
}
