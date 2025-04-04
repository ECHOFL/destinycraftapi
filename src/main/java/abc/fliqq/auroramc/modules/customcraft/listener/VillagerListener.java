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
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;

public class VillagerListener implements Listener {

    private final CustomCraftManager craftManager;

    public VillagerListener(CustomCraftManager craftManager) {
        this.craftManager = craftManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (!(e.getInventory() instanceof MerchantInventory)) return;

        MerchantInventory merchantInventory = (MerchantInventory) e.getInventory();
        Merchant merchant = merchantInventory.getMerchant();

        // Vérifier si le marchand est valide
        if (merchant == null) return;

        List<MerchantRecipe> recipes = new ArrayList<>(merchant.getRecipes());

        // Supprimer les recettes correspondant aux items bloqués
        recipes.removeIf(recipe -> isVillagerTradeBlocked(recipe.getResult()));

        // Mettre à jour les recettes du marchand
        merchant.setRecipes(recipes);
    }

    private boolean isVillagerTradeBlocked(ItemStack item) {
        for (CustomCraft craft : craftManager.getRecipes()) {
            if (!craft.isDisableVillagerTrade()) continue;

            ItemStack result = craft.getResult();

            // Vérifier si l'item est un livre enchanté
            if (result.getType() == Material.ENCHANTED_BOOK && item.getType() == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) result.getItemMeta();
                EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();

                // Comparer les enchantements stockés
                if (resultMeta != null && itemMeta != null && resultMeta.getStoredEnchants().equals(itemMeta.getStoredEnchants())) {
                    return true; // Bloquer si les enchantements correspondent
                }
            }

            // Vérifier les autres items (ignorer les flags, le lore et le nom)
            if (result.getType() == item.getType()) {
                return true; // Bloquer si le type correspond
            }
        }
        return false; // Autoriser l'item
    }
}