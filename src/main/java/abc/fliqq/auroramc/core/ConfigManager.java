package abc.fliqq.auroramc.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.util.LoggerUtil;

//Ecris les commentaires et messages en francais
public class ConfigManager {
    private final AuroraAPI plugin;
    private final Map<String, FileConfiguration> configs= new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();


    public ConfigManager(AuroraAPI plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getMainConfig(){
        return getConfig("config.yml");
    }
    public FileConfiguration getConfig(String name){
        return configs.get(name);
    }

    public FileConfiguration loadConfig(String name){
        File configFile = new File(plugin.getDataFolder(), name);
        
        plugin.getLogger().info("Chargement de la configuration: " + configFile.getAbsolutePath());

        // Vérifie si le dossier parent existe sinon crée le dossier
        if(!configFile.getParentFile().exists()){
            plugin.getLogger().info("Le dossier parent n'existe pas, création de " + configFile.getParentFile().getAbsolutePath());
        }

        // Crée le fichier de configuration s'il n'existe pas
        if(!configFile.exists()){
            plugin.getLogger().info("Le fichier de configuration n'existe pas, création de " + name);
            try{
                //Enregistre le fichier de configuration par défaut
                InputStream defaultConfigStream = plugin.getResource(name);
                if(defaultConfigStream != null){
                    plugin.getLogger().info("Enregistrement du fichier de configuration par défaut: " + name);
                    plugin.saveResource(name, false);
                 } else {
                    configFile.createNewFile();
                    plugin.getLogger().info("Le fichier de configuration par défaut n'existe pas, création de " + name);
                 }
            } catch (IOException e){
                plugin.getLogger().severe("Erreur lors de la création du fichier de configuration: " + name);
                e.printStackTrace();
                return null;
            }
        }
        // Chargement du fichier de configuration
        plugin.getLogger().info("Chargement du fichier de configuration du fichier: " + name);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        // check config valide
        if(config == null){
            plugin.getLogger().severe("Erreur lors du chargement du fichier de configuration: " + name);
            return null;
        }

        // Ajout du fichier de configuration aux listes
        configs.put(name, config);
        configFiles.put(name, configFile);
        plugin.getLogger().info("Fichier de configuration chargé avec succès: " + name);
        return config;
    }

    public FileConfiguration reloadConfig(String name){
        configs.remove(name);
        return loadConfig(name);
    }

    public void saveCOnfig(String name){
        FileConfiguration config = configs.get(name);
        File configFile = configFiles.get(name);
        if(config!=null && configFile != null){
            try {
                config.save(configFile);
                plugin.getLogger().info("Fichier de configuration enregistré avec succès: " + name);
            } catch (IOException e) {
                plugin.getLogger().severe("Erreur lors de l'enregistrement du fichier de configuration: " + name);
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().severe("Impossible d'enregistrer le fichier de configuration, le fichier n'existe pas: " + name);
        }
    }

    public FileConfiguration createModuleConfiguration(PluginModule module, String name){
        String modulePath = "modules/"+module.getName().toLowerCase()+"/"+name;

        File configFile = new File(plugin.getDataFolder(), modulePath);

        // Vérifie si le dossier parent existe sinon crée le dossier
        if(!configFile.getParentFile().exists()){
            plugin.getLogger().info("Le dossier parent n'existe pas, création de " + configFile.getParentFile().getAbsolutePath());
            configFile.getParentFile().mkdirs();
        }
        
        // Crée le fichier de configuration s'il n'existe pas
        if(!configFile.exists()){
            plugin.getLogger().info("Le fichier de configuration n'existe pas, création de " + modulePath);
            try{
                //Enregistre le fichier de configuration par défaut
                InputStream defaultConfigStream = plugin.getResource(modulePath);
                if(defaultConfigStream != null){
                    plugin.getLogger().info("Enregistrement du fichier de configuration par défaut: " + modulePath);
                    plugin.saveResource(modulePath, false);
                 } else {
                    configFile.createNewFile();
                    plugin.getLogger().info("Le fichier de configuration par défaut n'existe pas, création de " + modulePath);
                 }
            } catch (IOException e){
                LoggerUtil.severe("Erreur lors de la création du fichier de configuration: " + modulePath, e);
                e.printStackTrace();
                return null;
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(modulePath, config);
        configFiles.put(modulePath, configFile);
        plugin.getLogger().info("Fichier de configuration chargé avec succès: " + modulePath);
        return config;
    }

    public void reloadAllConfig(){
        for (String name : configs.keySet()){
            reloadConfig(name);
        }
    }
    
}
