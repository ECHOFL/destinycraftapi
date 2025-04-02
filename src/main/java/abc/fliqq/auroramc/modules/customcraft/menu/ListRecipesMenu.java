package abc.fliqq.auroramc.modules.customcraft.menu;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;
import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;

public class ListRecipesMenu extends Menu {
    public ListRecipesMenu(CustomCraftManager craftManager) {
        setTitle("§bRecettes existantes");
        setSize(54); // Taille maximale pour afficher jusqu'à 54 recettes

        int slot = 0;
        for (CustomCraft craft : craftManager.getRecipes()) {
            if (slot >= 54) break; // Limiter à 54 recettes

            addButton(new Button(slot++) {
                @Override
                public ItemStack getItem() {
                    return ItemBuilder.of(
                        craft.getResult().getType(),
                        craft.getResult().getItemMeta().getDisplayName(),
                        craft.getResult().getItemMeta().getLore() != null
                            ? craft.getResult().getItemMeta().getLore()
                            : Collections.emptyList()
                    ).make();
                }

                @Override
                public void onClick(Player player) {
                    player.sendMessage("§aVous avez sélectionné la recette : " + craft.getResult().getType());
                    // TODO : Ajouter un menu pour afficher les détails ou modifier la recette
                }
            });
        }
    }
}
