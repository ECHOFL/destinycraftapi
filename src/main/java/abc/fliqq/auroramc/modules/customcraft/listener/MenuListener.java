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
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if (player.hasMetadata("AuroraAPI")) {
            Menu menu = (Menu) player.getMetadata("AuroraAPI").get(0).value();
            if (menu == null) {
                player.sendMessage("Menu introuvable !");
                return;
            }
            for (Button button : menu.getButtons()) {
                if (button.getSlot() == slot) {
                    e.setCancelled(true); // Empêche les interactions par défaut
                    button.onClick(player);
                    return;
                }
            }
        } else {
            player.sendMessage("Aucun menu associé !");
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("AuroraAPI")) {
            player.removeMetadata("AuroraAPI", AuroraAPI.getInstance());
            AuroraAPI.getInstance().getLogger().info("Métadonné AuroraAPI supprimé pour le joueur : " + player.getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("AuroraAPI")) {
            player.removeMetadata("AuroraAPI", AuroraAPI.getInstance());
            AuroraAPI.getInstance().getLogger().info("Métadonné AuroraAPI supprimé pour le joueur : " + player.getName());
        }
    }
}
