
package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;

public class CustomCraftMenu extends Menu {

    private final CustomCraftManager customCraftManager;

    public CustomCraftMenu(CustomCraftModule module) {
        this.setTitle(MessageService.colorize("&7&lMenu recettes personnalisées"));

        customCraftManager = module.getCustomCraftManager();

        // Bouton pour afficher les recettes existantes
        this.addButton(new Button(12) {
            @Override
            public ItemStack getItem() {
                // Utilisation d'une liste vide pour le lore afin d'éviter une NullPointerException
                return ItemBuilder.of(Material.GOLDEN_APPLE, "&eAffiche les recettes personnalisées", Collections.emptyList()).make();
            }
            
            @Override
            public void onClick(Player player) {
                new ListRecipesMenu().displayTo(player);
            }
        });

        // Bouton pour créer une nouvelle recette
        this.addButton(new Button(14) {
            @Override
            public ItemStack getItem() {
                // Ici aussi, on utilise Collections.emptyList() pour le lore
                return ItemBuilder.of(Material.BOOK, "&aCrée une nouvelle recette", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                new CreateCraftMenu(CustomCraftMenu.this).displayTo(player);
            }  
        });
    }

    private class ListRecipesMenu extends Menu {
        public ListRecipesMenu() {
            this.setSize(9 * 6);
            this.setTitle(MessageService.colorize("&bRecettes actives"));
            int startingSlot = 0;
            for (CustomCraft craft : customCraftManager.getRecipes()) {

                if(startingSlot > (9 * 6)) return;

                // Incrémente le slot pour chaque recette
                this.addButton(new Button(++startingSlot) {
                    @Override
                    public ItemStack getItem() {
                        // Construction de l'item avec nom et lore tirés du craft
                        return ItemBuilder.of(
                            craft.getResult().getType(), 
                            craft.getResult().getItemMeta().getDisplayName(), 
                            // Utilisation du lore tel qu'il est (s'assurer qu'il n'est pas nul dans le craft)
                            craft.getResult().getItemMeta().getLore() != null ? craft.getResult().getItemMeta().getLore() : Collections.emptyList()
                        ).make();
                    }

                    @Override
                    public void onClick(Player player) {
                        // TODO: ouvrir un nouveau menu avec les détails du craft
                    }
                });
            }
        }
    }

    // Nouveau menu pour la création d'une recette personnalisée
    private class CreateCraftMenu extends Menu {
        private final CustomCraftMenu parentMenu;
        public CreateCraftMenu(CustomCraftMenu parentMenu) {
            this.setSize(9 * 4);
            this.parentMenu=parentMenu;
            this.setTitle(MessageService.colorize("&aCréer une nouvelle recette"));
            // TODO: Ajouter les boutons et logiques pour définir les ingrédients, le résultat, etc.
            // Exemple d'un bouton retour pour revenir au menu principal
            this.addButton(new Button(35) {
                @Override
                public ItemStack getItem() {
                    return ItemBuilder.of(Material.ARROW, "&cRetour", Collections.emptyList()).make();
                }

                @Override
                public void onClick(Player player) {
                    LoggerUtil.info("Bouton 'Retour' cliqué par : " + player.getName());
                    parentMenu.displayTo(player);

                }
            });
        }
    }
}
