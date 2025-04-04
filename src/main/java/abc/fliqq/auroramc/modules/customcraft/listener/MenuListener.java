package abc.fliqq.auroramc.modules.customcraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.customcraft.manager.CreationSessionManager;
import abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu;
import abc.fliqq.auroramc.modules.customcraft.menu.MainMenu;
import abc.fliqq.auroramc.modules.customcraft.menu.ShapeMenu;

public class MenuListener implements Listener {

    private final CustomCraftModule module;
    public MenuListener(CustomCraftModule module){
        this.module=module;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
    
        Player player = (Player) event.getWhoClicked();
        int rawSlot = event.getRawSlot(); // Slot brut (inclut les deux inventaires)
        int topInventorySize = event.getView().getTopInventory().getSize(); // Taille de l'inventaire du menu
    
        // Vérifier si une session existe
        CreationSession session = CreationSessionManager.getSession(player);
    
        if (session != null) {
            Menu menu = session.getCurrentMenu();
            if (menu instanceof ShapeMenu) {
    
                // Vérifier si le clic est dans l'inventaire personnel
                if (rawSlot >= topInventorySize) {
                    // Sélectionner l'item dans l'inventaire personnel
                    ItemStack selectedItem = event.getCurrentItem();
                    if (selectedItem != null && selectedItem.getType() != Material.AIR) {
                        session.setActiveItem(selectedItem.clone()); // Stocker l'item actif dans la session
                    } else {
                        session.setActiveItem(null); // Réinitialiser l'item actif
                    }
                    return; // Ne pas annuler le clic dans l'inventaire personnel
                }
    
                if (isGridSlot(rawSlot)) {
                    event.setCancelled(true); // Annuler l'interaction physique
                
                    // Convertir le slot en indices de ligne et de colonne
                    int[] rowCol = getRowColFromSlot(rawSlot);
                    int row = rowCol[0];
                    int col = rowCol[1];
                
                    // Copier l'item actif dans la grille ou vider le slot
                    ItemStack activeItem = session.getActiveItem();
                    if (activeItem != null) {
                        // Forcer la quantité de l'item à 1
                        ItemStack singleItem = activeItem.clone();
                        singleItem.setAmount(1);
                
                        session.setCraftSlot(row, col, singleItem);
                        ((ShapeMenu) menu).updateSlot(row, col, singleItem); // Mettre à jour le slot
                    } else {
                        // Si aucun item actif, vider le slot
                        session.clearCraftSlot(row, col);
                        ((ShapeMenu) menu).updateSlot(row, col, new ItemStack(Material.AIR)); // Vider le slot
                    }
                
                }
                 else if (rawSlot == 40) {
                    // Si le clic est sur le bouton "Retour"
                    event.setCancelled(true);
                    session.setShape(generateShapeFromGrid(session));
                    new CreationMenu(session).displayTo(player);
                } else {
                    // Annuler les clics hors de la grille 3x3 et du bouton "Retour"
                    event.setCancelled(true);
                }
            }
            else if (menu != null) {
                // Gestion générique pour les autres menus
                event.setCancelled(true); // Bloquer toutes les interactions
                Button button = menu.getButton(event.getSlot());
                if (button != null) {
                    button.onClick(player);
                }
            }
        }
    
        // Vérifier si le joueur est dans le MainMenu
        if (event.getView().getTitle().equals("§a§lMenu CustomCraft") || event.getView().getTitle().equals("§bRecettes existantes") || event.getView().getTitle().contains("§7Recette")) {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory.getHolder() instanceof Menu) {
                event.setCancelled(true); // Bloquer toutes les interactions par défaut
        
                Menu menu = (Menu) inventory.getHolder();
                Button button = menu.getButton(event.getSlot());
                if (button != null) {
                    button.onClick(player);
                }
            }
        }
    }

    private int[] getRowColFromSlot(int slot) {
        int[] gridSlots = {12, 13, 14, 21, 22, 23, 30, 31, 32};
        for (int i = 0; i < gridSlots.length; i++) {
            if (gridSlots[i] == slot) {
                return new int[]{i / 3, i % 3}; // Retourne {row, col}
            }
        }
        throw new IllegalArgumentException("Slot non valide pour la grille : " + slot);
    }
    private String[] generateShapeFromGrid(CreationSession session) {
        // Liste des slots fixes de la grille 3x3
        int[] gridSlots = {12, 13, 14, 21, 22, 23, 30, 31, 32};
        String[] shape = new String[3];
    
        for (int row = 0; row < 3; row++) {
            StringBuilder rowShape = new StringBuilder();
            for (int col = 0; col < 3; col++) {
                int slot = gridSlots[row * 3 + col]; // Calculer le slot correspondant
                char key = (char) ('A' + row * 3 + col); // Générer la clé correspondante ('A', 'B', ..., 'I')
    
                // Vérifier si un item est présent dans le slot
                if (session.getIngredients().containsKey(key)) {
                    rowShape.append(key); // Ajouter la clé si un item est présent
                } else {
                    rowShape.append(" "); // Ajouter un espace si le slot est vide
                }
            }
            shape[row] = rowShape.toString(); // Ajouter la ligne à la shape
        }
    
        return shape;
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        // Vérifier si le joueur est dans un menu personnalisé
        if (event.getView().getTitle().equals("§a§lMenu CustomCraft") || event.getView().getTitle().equals("§bRecettes existantes") || event.getView().getTitle().contains("§7Recette") ) { // Titre du MainMenu
            event.setCancelled(true); // Bloquer les glisser-déposer
        }

        CreationSession session = CreationSessionManager.getSession(player);
        if (session != null) {
            Menu menu = session.getCurrentMenu();
            if (menu instanceof ShapeMenu) {
                // Vérifier si tous les slots affectés par le drag sont dans la grille 3x3
                boolean isValidDrag = event.getRawSlots().stream().allMatch(this::isGridSlot);
                if (isValidDrag) {
                    // Autoriser le drag-and-drop dans la grille 3x3
                    event.setCancelled(false);
                } else {
                    // Bloquer le drag-and-drop sur le contour ou en dehors
                    event.setCancelled(true);
                }
            } else {
                // Bloquer les drag-and-drop dans les autres menus
                event.setCancelled(true);
            }
            return;
        }
    }

    
        @EventHandler
        public void onInventoryInteract(InventoryInteractEvent event) {
            if (!(event.getWhoClicked() instanceof Player)) return;
        
            Player player = (Player) event.getWhoClicked();
        
            // Vérifier si une session existe
            CreationSession session = CreationSessionManager.getSession(player);
            if (session != null) {
                Menu menu = session.getCurrentMenu();
                if (menu instanceof ShapeMenu) {
                    // Ne rien faire ici, car les interactions spécifiques sont déjà gérées
                    return;
                } else if (menu != null) {
                    // Bloquer toutes les interactions dans les autres menus
                    event.setCancelled(true);
                }
            }
        
            // Vérifier si le joueur est dans le MainMenu
            if (event.getView().getTitle().equals("§a§lMenu CustomCraft") || event.getView().getTitle().equals("§bRecettes existantes") || event.getView().getTitle().contains("§7Recette")) {
                event.setCancelled(true); // Bloquer toutes les interactions par défaut
            }
        }
    // Supprimer la session de création à la fermeture du menu de création
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
    
        Player player = (Player) event.getPlayer();
        CreationSession session = CreationSessionManager.getSession(player);
    
        if (session != null) {
    
            // Ne pas supprimer la session si le joueur attend une entrée
            if (session.getAwaitingInput() != null) {
                return;
            }
    
            // Vérifier après 5 ticks si le joueur est toujours dans un menu personnalisé
            Bukkit.getScheduler().runTaskLater(AuroraAPI.getInstance(), () -> {
                Inventory openInventory = player.getOpenInventory().getTopInventory();
    
                if (openInventory == null || !(openInventory.getHolder() instanceof Menu)) {
                    // Si le joueur a quitté le CreationMenu et n'est plus dans un menu, supprimer la session
                    if (session.getCurrentMenu() instanceof CreationMenu) {
                        LoggerUtil.info("Le joueur " + player.getName() + " a quitté le CreationMenu. Suppression de la session.");
                        CreationSessionManager.removeSession(player);
                    } else {
                        // Le joueur a quitté un sous-menu, rouvrir le CreationMenu
                        LoggerUtil.info("Le joueur " + player.getName() + " a quitté un sous-menu. Réouverture du CreationMenu.");
                        if (player.isOnline()) {
                            new CreationMenu(session).displayTo(player);
                        }
                    }
                } else {
                    // Le joueur est toujours dans un menu personnalisé, ne rien faire
                    LoggerUtil.info("Le joueur " + player.getName() + " est toujours dans un menu personnalisé. Aucune action nécessaire.");
                }
            }, 5L); // Vérifier après 5 ticks
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CreationSession session = CreationSessionManager.getSession(player);
    
        if (session != null) {
            CreationSessionManager.removeSession(player);
        } 
    }

        private boolean isGridSlot(int slot) {
            // Liste des slots de la grille 3x3
            int[] gridSlots = {12, 13, 14, 21, 22,23, 30, 31, 32};
            for (int gridSlot : gridSlots) {
                if (slot == gridSlot) {
                    return true;
                }
            }
            return false;
        }

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
    
                // Utiliser un scheduler pour retarder la réinitialisation et la réouverture du menu
                Bukkit.getScheduler().runTaskLater(AuroraAPI.getInstance(), () -> {
                    session.setAwaitingInput(null); // Réinitialiser l'état
                    new abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu(session).displayTo(player);
                }, 3L); // Attendre 5 ticks avant de réinitialiser
                return;
            }
    
            // Gestion du lore
            if (session.getAwaitingInput() == CreationSession.InputType.LORE) {
                if (message.equalsIgnoreCase("terminer")) {
                    player.sendMessage("§aLore terminé.");
    
                    // Utiliser un scheduler pour retarder la réinitialisation et la réouverture du menu
                    Bukkit.getScheduler().runTaskLater(AuroraAPI.getInstance(), () -> {
                        session.setAwaitingInput(null); // Réinitialiser l'état
                        new abc.fliqq.auroramc.modules.customcraft.menu.CreationMenu(session).displayTo(player);
                    }, 3L); // Attendre 5 ticks avant de réinitialiser
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