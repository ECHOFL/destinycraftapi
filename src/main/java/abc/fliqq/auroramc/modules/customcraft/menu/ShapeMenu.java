package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class ShapeMenu extends Menu {
    private final CreationSession session;

    public ShapeMenu(CreationSession session) {
        this.session = session;
        setTitle("§aDéfinir la forme de la recette");
        setSize(5 * 9); // Menu 5x9 (45 slots)

        // Ajouter un contour en verre
        for (int i = 0; i < getSize(); i++) {
            if (isBorderSlot(i)) {
                addButton(new Button(i) {
                    @Override
                    public ItemStack getItem() {
                        return ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE, " ", Collections.emptyList()).make();
                    }

                    @Override
                    public void onClick(Player player) {
                        // Ne rien faire pour les clics sur le contour
                    }
                });
            }
        }

        // Ajouter les slots de la grille 3x3
        int[] gridSlots = {12, 13, 14, 21, 22, 23, 30, 31, 32};
        for (int index = 0; index < gridSlots.length; index++) {
            int slot = gridSlots[index];
            final int finalRow = index / 3; // Calculer la ligne dans la grille 3x3
            final int finalCol = index % 3; // Calculer la colonne dans la grille 3x3

            addButton(new Button(slot) {
                @Override
                public ItemStack getItem() {
                    // Afficher l'item correspondant dans la grille
                    char key = getKeyFromSlot(finalRow, finalCol);
                    ItemStack item = session.getIngredients().get(key);
                    return item != null ? item : new ItemStack(Material.AIR);
                }

                @Override
                public void onClick(Player player) {
                    // Ne rien faire ici, la logique est gérée dans le MenuListener
                }
            });
        }

        // Bouton retour
        addButton(new Button(40) { // Slot central en bas
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ARROW, "§cRetour", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                // Ne rien faire ici, la logique est gérée dans le MenuListener
            }
        });
    }

    private boolean isBorderSlot(int slot) {
        // Liste des slots du contour
        int[] borderSlots = {2, 3, 4, 5, 6, 11, 20, 29, 38, 39, 41, 42, 33, 24, 15};
        for (int borderSlot : borderSlots) {
            if (slot == borderSlot) {
                return true;
            }
        }
        return false;
    }

    private char getKeyFromSlot(int row, int col) {
        return (char) ('A' + row * 3 + col);
    }

    public void updateSlot(int row, int col, ItemStack item) {
        int[] gridSlots = {12, 13, 14, 21, 22, 23, 30, 31, 32};
        int slot = gridSlots[row * 3 + col]; // Calculer le slot correspondant
        getInventory().setItem(slot, item); // Mettre à jour l'item dans l'inventaire
    }
}