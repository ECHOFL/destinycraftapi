package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;

public class CreationMenu extends Menu {
    private final CreationSession session;
    private final CustomCraftModule module = (CustomCraftModule) AuroraAPI.getInstance().getModuleManager().getModule("CustomCraft");

    public CreationMenu(CreationSession session) {
        this.session = session;
        setTitle("§aCréation d'un item");
        setSize(27);

        // Aperçu de l'item au centre
        addButton(new Button(13) {
            @Override
            public ItemStack getItem() {
                return session.getBaseItem();
            }

            @Override
            public void onClick(Player player) {
                player.sendMessage("§eAperçu de l'item en cours de création.");
            }
        });

        // Bouton pour modifier le nom
        addButton(new Button(10) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.NAME_TAG, "§eModifier le nom", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                session.setAwaitingInput(CreationSession.InputType.NAME); // Attendre une entrée pour le nom
                player.closeInventory(); // Fermer le menu
                player.sendMessage("§aEntrez le nouveau nom dans le chat.");

            }
        });
        // Bouton pour modifier le lore
        addButton(new Button(11) { // Slot 11
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.BOOK, "§eModifier le lore", Collections.emptyList()).make();
            }
        
            @Override
            public void onClick(Player player) {
                session.setAwaitingInput(CreationSession.InputType.LORE); // Définir l'état avant de fermer le menu
                player.closeInventory(); // Fermer le menu
                player.sendMessage("§aEntrez les lignes du lore dans le chat. Tapez 'terminer' pour finir.");
            }
        });

        // Bouton pour ajouter des enchantements
        addButton(new Button(12) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ENCHANTED_BOOK, "§eAjouter des enchantements", Collections.emptyList()).make();
            }
        
            @Override
            public void onClick(Player player) {
                new EnchantMenu(session).displayTo(player);
            }
        });

        //FLAGS
        addButton(new Button(14) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.BARRIER, "§eAjouter des flags", Collections.emptyList()).make();
            }
        
            @Override
            public void onClick(Player player) {
                new FlagMenu(session).displayTo(player);
            }
        });

        // Bouton pour définir la recette
    // Bouton pour définir la recette
    addButton(new Button(15) {
        @Override
        public ItemStack getItem() {
            return ItemBuilder.of(Material.CRAFTING_TABLE, "§eDéfinir la recette", Collections.emptyList()).make();
        }

        @Override
        public void onClick(Player player) {
            new ShapeMenu(session).displayTo(player);
        }
    });
    // Bouton pour finaliser la création
    addButton(new Button(16) {
        @Override
        public ItemStack getItem() {
            return ItemBuilder.of(Material.EMERALD, "§aFinaliser la création", Collections.emptyList()).make();
        }
        @Override
        public void onClick(Player player) {
            // Vérifier si la shape est valide
            if (!isShapeValid()) {
                player.sendMessage("§cVous devez définir une forme valide avant de finaliser !");
                return;
            }

            // Créer un objet CustomCraft à partir de la session
            CustomCraft craft = session.toCustomCraft();

            // Ajouter la recette au CustomCraftManager
            CustomCraftManager manager = module.getCustomCraftManager();
            manager.addRecipe(craft);

            player.sendMessage("§aRecette finalisée et sauvegardée !");
            player.closeInventory();
        }

        private boolean isShapeValid() {
            String[] shape = session.getShape();
            for (String row : shape) {
                if (!row.trim().isEmpty()) {
                    return true; // Au moins une ligne contient des caractères
                }
            }
            return false; // Toutes les lignes sont vides
        }
    });
    }
}
