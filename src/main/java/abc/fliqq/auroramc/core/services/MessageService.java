package abc.fliqq.auroramc.core.services;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.PluginModule;
import net.md_5.bungee.api.ChatColor;

public class MessageService {
    private final AuroraAPI plugin;
    private FileConfiguration messages;

    // Plugin prefix et prefix des modules
    private String prefix;
    private final Map<String, String> modulePrefixes = new HashMap<>();

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public MessageService(AuroraAPI plugin) {
        this.plugin = plugin;
        reload();
    }

    private void reload(){
        messages = plugin.getConfigManager().getConfig("messages.yml");
        // Vérifie si le fichier de configuration des messages a été chargé avec succès
        if(messages == null){
            plugin.getLogger().warning("Le fichier de configuration des messages n'a pas pu être chargé.");
            return;
            //ajouter fichier messages par defaut ?
        }

        prefix = colorize(messages.getString("general.prefix", "&7[&6Aurora&6API&7] &r"));

        modulePrefixes.clear();
        for (String key : messages.getKeys(false)){
            if(messages.isConfigurationSection(key) && messages.contains(key + ".prefix")){
                String modulePrefix = messages.getString(key + ".prefix");
                if(modulePrefix != null){
                    modulePrefixes.put(key, modulePrefix);
                }
            }
        }
    }

    public String getModulePrefix(String module){
        return modulePrefixes.getOrDefault(module.toLowerCase(), prefix);
    }
    public String getModulePrefix(PluginModule module){
        return modulePrefixes.getOrDefault(module.getName().toLowerCase(), prefix);
    }

    public void sendMessage(CommandSender sender, String key){
        sendMessage(sender, key, new HashMap<>());
    }

    public void sendMessage(CommandSender sender, String key, Map<String, String> placeholders){
        String message = getMessage(key, placeholders);
        if(!message.isEmpty()){
            sender.sendMessage(message);
        }
    }
    public void broadcastMessage(String key) {
        broadcastMessage(key, new HashMap<>());
    }
    public void broadcastMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key, placeholders);
        if (!message.isEmpty()) {
            Bukkit.broadcastMessage(message);
        
        }
    }

    public String getMessage(String key) {
        return getMessage(key, new HashMap<>());
    }
    
    public String getMessage(String key, Map<String, String> placeholders) {
        String message = messages.getString(key);
        if (message == null) {
            return "";
        }
        String currentPrefix = prefix;
        String[] parts = key.split("\\.");
        if (parts.length > 1 && modulePrefixes.containsKey(parts[0])) {
            currentPrefix = modulePrefixes.get(parts[0]);
        }
        if (!message.startsWith(currentPrefix) && !key.endsWith(".prefix")) {
            message = currentPrefix + message;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return colorize(message);

    }

    public String colorize(String text){
        if (text == null) {
            return "";
        }

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + hexColor).toString());
        }

        matcher.appendTail(buffer);
        text = buffer.toString();
   
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String stripColor(String text){
        if (text == null) {
            return "";
        }
        text = HEX_PATTERN.matcher(text).replaceAll("");
        
        return ChatColor.stripColor(text);
    }

    public void sendTitle(Player player, String titleKey, String subtitleKey, int fadeIn, int stay, int fadeOut) {
        sendTitle(player, titleKey, subtitleKey, new HashMap<>(), fadeIn, stay, fadeOut);
    }

    public void sendTitle(Player player, String titleKey, String subtitleKey, Map<String, String> placeholders, int fadeIn, int stay, int fadeOut) {
        String title = getMessage(titleKey, placeholders);
        String subtitle = getMessage(subtitleKey, placeholders);
        
        // Remove any prefixes for titles
        if (title.startsWith(prefix)) {
            title = title.substring(prefix.length());
        }
        
        if (subtitle.startsWith(prefix)) {
            subtitle = subtitle.substring(prefix.length());
        }
        
        // Remove module prefixes
        for (String modulePrefix : modulePrefixes.values()) {
            if (title.startsWith(modulePrefix)) {
                title = title.substring(modulePrefix.length());
            }
            if (subtitle.startsWith(modulePrefix)) {
                subtitle = subtitle.substring(modulePrefix.length());
            }
        }
        
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendActionBar(Player player, String key) {
        sendActionBar(player, key, new HashMap<>());
    }
    
    public void sendActionBar(Player player, String key, Map<String, String> placeholders) {
        String message = getMessage(key, placeholders);
        
        // Remove any prefixes for action bars
        if (message.startsWith(prefix)) {
            message = message.substring(prefix.length());
        }
        
        // Remove module prefixes
        for (String modulePrefix : modulePrefixes.values()) {
            if (message.startsWith(modulePrefix)) {
                message = message.substring(modulePrefix.length());
            }
        }
        
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
                                   net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
    }

    public String format(String message, Map<String, String> placeholders){
        if (message == null) {
            return "";
        }
        for(Map.Entry<String,String> entry : placeholders.entrySet()){
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return colorize(message);
    }
}

