package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class CreationMenu extends Menu {
    private final CreationSession session;

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
                player.closeInventory();
                player.sendMessage("§aEntrez le nouveau nom dans le chat.");
                session.setName(null); // Placeholder pour attendre l'entrée du joueur
            }
        });

        // Bouton pour modifier le lore
        addButton(new Button(11) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.BOOK, "§eModifier le lore", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                player.closeInventory();
                player.sendMessage("§aEntrez une ligne de lore dans le chat (ou 'terminer' pour finir).");
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
        addButton(new Button(15) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.CRAFTING_TABLE, "§eDéfinir la recette", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                player.sendMessage("§aOuverture du menu de la recette...");
                // TODO : Ouvrir un menu pour gérer la grille de craft
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
                player.sendMessage("§aCréation de l'item finalisée !");
                // TODO : Convertir la session en CustomCraft et l'ajouter au manager
            }
        });
    }
}
