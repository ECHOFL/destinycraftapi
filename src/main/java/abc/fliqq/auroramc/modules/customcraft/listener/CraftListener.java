package abc.fliqq.auroramc.modules.customcraft.listener;

import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;

import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {

    private final CustomCraftManager craftManager;

    public CraftListener(CustomCraftManager craftManager) {
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();

        // Vérifier si le résultat est bloqué
        if (result != null && isCraftBlocked(result, inventory)) {
            inventory.setResult(null); // Bloquer le craft en supprimant le résultat
        }
    }

    private boolean isCraftBlocked(ItemStack item, CraftingInventory inventory) {
        boolean isCustomCraft = false;

        // Vérifier si c'est une recette personnalisée
        for (CustomCraft craft : craftManager.getRecipes()) {
            if (isCustomCraft(craft, inventory)) {
                isCustomCraft = true;
                break;
            }
        }

        if (isCustomCraft) {
            return false; // Autoriser les recettes personnalisées
        }

        // Bloquer les crafts naturels si désactivés
        for (CustomCraft craft : craftManager.getRecipes()) {
            if (craft.isDisableNaturalCraft() && item.getType() == craft.getResult().getType()) {
                return true; // Bloquer le craft naturel
            }
        }

        return false; // Autoriser les autres crafts
    }

    private boolean isCustomCraft(CustomCraft craft, CraftingInventory inventory) {
        // Vérifier si la forme et les ingrédients correspondent à une recette personnalisée
        String[] shape = craft.getShape();
        Map<Character, ItemStack> ingredients = craft.getIngredients();

        // Vérifier chaque ligne de la grille de craft
        for (int row = 0; row < shape.length; row++) {
            String line = shape[row];
            for (int col = 0; col < line.length(); col++) {
                char key = line.charAt(col);
                ItemStack expected = ingredients.get(key);
                ItemStack actual = inventory.getMatrix()[row * 3 + col]; // Grille 3x3

                // Si l'ingrédient attendu ne correspond pas à l'ingrédient actuel
                if (key != ' ' && (expected == null || !isSimilarIgnoreMeta(expected, actual))) {
                    return false; // Ce n'est pas une recette personnalisée
                }
            }
        }
        return true; // La recette correspond à une recette personnalisée
    }

    private boolean isSimilarIgnoreMeta(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) return false;
        return item1.getType() == item2.getType(); // Comparer uniquement le type
    }
}