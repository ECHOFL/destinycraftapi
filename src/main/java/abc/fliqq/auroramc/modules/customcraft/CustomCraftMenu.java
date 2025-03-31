package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;

public class CustomCraftMenu extends Menu {

    private final CustomCraftManager customCraftManager;

    public CustomCraftMenu(CustomCraftModule module) {
        this.setTitle(MessageService.colorize("&7&lMenu recettes personnalisées"));

        customCraftManager = module.getCustomCraftManager();

        // Bouton pour afficher les recettes existantes
        this.addButton(new Button(12) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.GOLDEN_APPLE, "&eAffiche les recettes personnalisées", null).make();
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
                return ItemBuilder.of(Material.BOOK, "&aCrée une nouvelle recette", null).make();
            }

            @Override
            public void onClick(Player player) {
                new CreateCraftMenu().displayTo(player);
            }  
        });
    }

    private class ListRecipesMenu extends Menu {
        public ListRecipesMenu() {
            this.setSize(9 * 6);
            this.setTitle(MessageService.colorize("&bRecettes actives"));
            int startingSlot = 0;
            for (CustomCraft craft : customCraftManager.getRecipes()) {

                if (startingSlot > (9 * 6)) return;

                // Incrémente le slot pour chaque recette
                this.addButton(new Button(++startingSlot) {
                    @Override
                    public ItemStack getItem() {
                        // Construction de l'item avec nom et lore s'ils existent
                        return ItemBuilder.of(
                            craft.getResult().getType(), 
                            craft.getResult().getItemMeta().getDisplayName(), 
                            craft.getResult().getItemMeta().getLore()
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
        public CreateCraftMenu() {
            this.setSize(9 * 4);
            this.setTitle(MessageService.colorize("&aCréer une nouvelle recette"));
            // TODO: Ajouter les boutons et logiques pour définir les ingrédients, le résultat, etc.
            // Ex: un bouton pour sélectionner l'ingrédient, un autre pour définir la quantité, etc.

            // Exemple d'un bouton retour pour revenir au menu principal
            this.addButton(new Button(35) {
                @Override
                public ItemStack getItem() {
                    return ItemBuilder.of(Material.ARROW, "&cRetour", null).make();
                }

                @Override
                public void onClick(Player player) {
                    new CustomCraftMenu((CustomCraftModule) AuroraAPI.getInstance().getModuleManager().getModule("customcraft")).displayTo(player);
                }
            });
        }
    }
}