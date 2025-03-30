package abc.fliqq.auroramc;

import org.bukkit.plugin.java.JavaPlugin;

import abc.fliqq.auroramc.core.ConfigManager;
import abc.fliqq.auroramc.core.ModuleManager;
import abc.fliqq.auroramc.core.PluginModule;
import abc.fliqq.auroramc.core.services.MessageService;
import lombok.Getter;

public class AuroraAPI extends JavaPlugin {

    @Getter private ModuleManager moduleManager;
    @Getter private MessageService messageService;
    @Getter private ConfigManager configManager;


    @Override
    public void onEnable() {

        //initialize config manager
        configManager = new ConfigManager(this);
        //charger les fichiers de configuration
        loadPluginConfigurations();

        // initialize message service
        messageService = new MessageService(this);

        //initialize module manager
        moduleManager = new ModuleManager();

        //initialize modules
        initializeModules();


        getLogger().info("AuroraAPI a été activé");
    }
    @Override
    public void onDisable() {
        getLogger().info("AuroraAPI désactivé");
    }
    
    private void initializeModules() {
        for(PluginModule module : moduleManager.getModules()) {
            module.onInit();
        }
    }
    private void loadPluginConfigurations() {
        configManager.loadConfig("config.yml");
        configManager.loadConfig("messages.yml");
    }

    public static AuroraAPI getInstance() {
        return getPlugin(AuroraAPI.class);
    }
}