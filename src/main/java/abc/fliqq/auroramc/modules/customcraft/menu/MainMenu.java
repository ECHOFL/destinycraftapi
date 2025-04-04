package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.customcraft.manager.CreationSessionManager;

public class MainMenu extends Menu {
    public MainMenu(CustomCraftModule module){
        setTitle(MessageService.colorize("&a&lMenu CustomCraft"));
        setSize(27);
        addButton(new Button(12) {

            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.CHEST, "&eVoir les crafts existants", Collections.emptyList()).make();

            }

            @Override
            public void onClick(Player player) {
                new ListRecipesMenu(module).displayTo(player);
            }
            
        });

        addButton(new Button(14) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ANVIL, "&aCréer un nouvel item", Collections.emptyList()).make();
            }
        
            @Override
            public void onClick(Player player) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                    player.sendMessage(MessageService.colorize(module.getModulePrefix()+"&cVous devez tenir un item en main pour commencer !"));
                    return;
                }
        
                // Vérifier si une session existe déjà
                if (CreationSessionManager.hasSession(player)) {
                    player.sendMessage(MessageService.colorize(module.getModulePrefix()+"&cVous avez déjà une session active !"));
                    return;
                }
        
                // Créer une nouvelle session
                CreationSession session = new CreationSession(player, itemInHand);
                CreationSessionManager.addSession(player, session);
        
                // Ouvrir le menu de création
                new CreationMenu(session).displayTo(player);
            }
        });
    }
    
}
