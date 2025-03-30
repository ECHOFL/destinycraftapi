package abc.fliqq.auroramc;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import abc.fliqq.auroramc.core.ConfigManager;
import abc.fliqq.auroramc.core.ModuleManager;
import abc.fliqq.auroramc.core.PluginModule;
import abc.fliqq.auroramc.core.services.DatabaseConnector;
import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.duel.DuelModule;
import lombok.Getter;

public class AuroraAPI extends JavaPlugin {

    @Getter private ModuleManager moduleManager;
    @Getter private MessageService messageService;
    @Getter private ConfigManager configManager;
    @Getter private DatabaseConnector databaseConnector;

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

        //register modules
        registerModules();
        
        //initialize modules
        initializeModules();

        //initialize database connector
        //databaseConnector = new DatabaseConnector(configManager.getMainConfig());

        getLogger().info("AuroraAPI a été activé");
    }
    @Override
    public void onDisable() {
        getLogger().info("AuroraAPI désactivé");
    }
    
    private void registerModules(){
        moduleManager.registerModule(new DuelModule(this));
        moduleManager.registerModule(new CustomCraftModule(this));
    }
    private void initializeModules() {
        for (PluginModule module : new ArrayList<>(moduleManager.getModules())) {
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