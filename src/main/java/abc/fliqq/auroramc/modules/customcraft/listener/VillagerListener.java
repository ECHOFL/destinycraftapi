package abc.fliqq.auroramc.modules.customcraft.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;

public class VillagerListener implements Listener {

    private final CustomCraftModule module;

    public VillagerListener(CustomCraftModule  module) {
        this. module =  module;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if (!(e.getInventory() instanceof MerchantInventory)) return;
        MerchantInventory merchantInventory = (MerchantInventory) e.getInventory();
        Merchant merchant = merchantInventory.getMerchant();

        // check si marchant vanilla 
        if (merchant == null) return;

        List<MerchantRecipe> recipes = new ArrayList<>(merchant.getRecipes());

        recipes.removeIf(recipe -> {
            ItemStack result = recipe.getResult();
            // Vérifie que l'item est un livre et qu'il possède l'enchantement MENDING
            if ((result.getType() == Material.WRITTEN_BOOK || result.getType() == Material.BOOK) && result.hasItemMeta()) {
                ItemMeta meta = result.getItemMeta();
                return meta.hasEnchant(Enchantment.MENDING);
            }
            return false;
        });
        merchant.setRecipes(recipes);

    }
    
}
