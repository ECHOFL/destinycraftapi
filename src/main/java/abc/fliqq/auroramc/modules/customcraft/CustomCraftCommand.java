package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import abc.fliqq.auroramc.core.services.MessageService;

public class CustomCraftCommand implements CommandExecutor {

    private final CustomCraftModule customCraftModule;

    // Injection du module CustomCraftModule via le constructeur
    public CustomCraftCommand(CustomCraftModule customCraftModule) {
        this.customCraftModule = customCraftModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageService.colorize("&cSeul les joueurs peuvent exécuter cette commande."));
            return true;
        }
        Player player = (Player) sender;
        new CustomCraftMenu(customCraftModule).displayTo(player);
        return true;
    }
}