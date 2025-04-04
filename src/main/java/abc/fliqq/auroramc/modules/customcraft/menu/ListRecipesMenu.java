package abc.fliqq.auroramc.modules.customcraft.menu;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;

public class ListRecipesMenu extends Menu {
    private CustomCraftManager craftManager;
    public ListRecipesMenu(CustomCraftModule module) {
        craftManager = module.getCustomCraftManager();
        setTitle("§bRecettes existantes");
        setSize(54); // Taille maximale pour afficher jusqu'à 54 recettes

        int slot = 0;
        for (CustomCraft craft : craftManager.getRecipes()) {
            if (slot >= 54) break; // Limiter à 54 recettes

            addButton(new Button(slot++) {
                @Override
                public ItemStack getItem() {
                    return craft.getResult();
                }

                @Override
                public void onClick(Player player) {
                    player.sendMessage("§aVous avez sélectionné la recette : " + craft.getResult().getItemMeta().getDisplayName());
                    new RecipeViewMenu(craft, craftManager, module).displayTo(player);
                }
            });
        }

        addButton(new Button(53) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ARROW, "§cRetour", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                // Retourner au menu précédent
                new MainMenu(module).displayTo(player);
            }
        });
    }
}
