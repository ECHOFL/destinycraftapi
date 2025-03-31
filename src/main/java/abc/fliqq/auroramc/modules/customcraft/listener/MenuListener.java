package abc.fliqq.auroramc.modules.customcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;

public class MenuListener implements Listener{
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if(player.hasMetadata("AuroraAPI")){
            Menu menu = (Menu) player.getMetadata("AuroraAPI").get(0).value();
            for(Button button : menu.getButtons()){
                if(button.getSlot() == slot){
                    button.onClick(player);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if(player.hasMetadata("AuroraAPI")){
            player.removeMetadata("AuroraAPI", AuroraAPI.getInstance());
        }
    }
    @EventHandler
    public void onInventoryClose(PlayerQuitEvent event){
        Player player = (Player) event.getPlayer();

        if(player.hasMetadata("AuroraAPI")){
            player.removeMetadata("AuroraAPI", AuroraAPI.getInstance());
        }
    } 
}
