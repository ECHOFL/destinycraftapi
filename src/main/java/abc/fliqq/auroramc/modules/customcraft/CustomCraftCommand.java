package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.modules.customcraft.menu.MainMenu;

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
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("customcraft.reload")) {
                    sender.sendMessage(MessageService.colorize(customCraftModule.getModulePrefix() + "&cVous n'avez pas la permission d'exécuter cette commande."));
                    return true;
                }
                customCraftModule.onReload();
                sender.sendMessage(MessageService.colorize(customCraftModule.getModulePrefix() + "&aLe module a été rechargé avec succès."));
                return true;
            }
            sender.sendMessage(MessageService.colorize(customCraftModule.getModulePrefix()+"&cUsage: /customcraft <reload> ou /customcraft"));
            return true;
        }
        Player player = (Player) sender;
        new MainMenu(customCraftModule).displayTo(player);
        return true;
    }
}