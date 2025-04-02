package abc.fliqq.auroramc.modules.customcraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.manager.CreationSessionManager;

public class MenuListener implements Listener {

    // Empêcher les interactions non prévues dans les menus
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        CreationSession session = CreationSessionManager.getSession(player);

        if (session != null) {
            event.setCancelled(true); // Bloquer toutes les interactions par défaut

            // Récupérer le menu actuel
            Menu menu = session.getCurrentMenu();
            if (menu != null) {
                // Gérer les clics sur les boutons
                Button button = menu.getButton(event.getSlot());
                if (button != null) {
                    button.onClick(player);
                }
            }
        }
    }

    // Supprimer la session de création à la fermeture du menu de création
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        CreationSession session = CreationSessionManager.getSession(player);

        if (session != null) {
            // Supprimer la session si le menu actuel est un menu de création
            if (session.getCurrentMenu() instanceof abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu) {
                CreationSessionManager.removeSession(player);
            }
        }
    }

    // Supprimer la session de création à la déconnexion du joueur
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CreationSessionManager.removeSession(player);
    }

    // Gérer les entrées via le chat pour le nom et le lore
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        CreationSession session = CreationSessionManager.getSession(player);

        if (session != null) {
            event.setCancelled(true); // Bloquer le message dans le chat public

            String message = event.getMessage();

            // Convertir les codes de couleur & en §
            message = MessageService.colorize(message);

            // Gestion du nom
            if (session.getAwaitingInput() == CreationSession.InputType.NAME) {
                session.setName(message);
                session.updateBaseItem();
                player.sendMessage("§aNom défini : §e" + message);
                session.setAwaitingInput(null); // Réinitialiser l'état
                new abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu(session).displayTo(player);
                return;
            }

            // Gestion du lore
            if (session.getAwaitingInput() == CreationSession.InputType.LORE) {
                if (message.equalsIgnoreCase("terminer")) {
                    player.sendMessage("§aLore terminé.");
                    session.setAwaitingInput(null); // Réinitialiser l'état
                    new abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu(session).displayTo(player);
                } else {
                    session.addLoreLine(message);
                    session.updateBaseItem();
                    player.sendMessage("§aLigne ajoutée au lore : §e" + message);
                }
                return;
            }
        }
    }
}