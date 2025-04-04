package abc.fliqq.auroramc.modules.customcraft.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;
import abc.fliqq.auroramc.modules.customcraft.manager.CustomCraftManager;

import java.util.Collections;
import java.util.Map;

public class RecipeViewMenu extends Menu {

    private final CustomCraft recipe;
    private final CustomCraftManager craftManager;
    private final CustomCraftModule module;

    public RecipeViewMenu(CustomCraft recipe, CustomCraftManager craftManager, CustomCraftModule module) {
        this.recipe = recipe;
        this.module= module;
        this.craftManager=craftManager;
        setTitle("§7Recette : " + recipe.getResult().getItemMeta().getDisplayName());
        setSize(45); // Taille du menu (3 lignes)
        setupMenu();
    }

    private void setupMenu() {
        // Grille 3x3 pour afficher la recette
        String[] shape = recipe.getShape();
        Map<Character, ItemStack> ingredients = recipe.getIngredients();

        int[] gridSlots = {1, 2, 3, 10, 11, 12, 19, 20, 21}; // Slots correspondant à la grille 3x3
        for (int i = 0; i < shape.length; i++) {
            String row = shape[i];
            for (int j = 0; j < row.length(); j++) {
                char ingredientChar = row.charAt(j);
                int slot = gridSlots[i * 3 + j];
                if (ingredientChar != ' ' && ingredients.containsKey(ingredientChar)) {
                    addButton(new Button(slot) {
                        @Override
                        public ItemStack getItem() {
                            return ingredients.get(ingredientChar);
                        }

                        @Override
                        public void onClick(Player player) {
                            // Ne rien faire lors du clic sur un ingrédient
                        }
                    });
                } else {
                    addButton(new Button(slot) {
                        @Override
                        public ItemStack getItem() {
                            return new ItemStack(Material.AIR); // Slot vide
                        }

                        @Override
                        public void onClick(Player player) {
                            // Ne rien faire
                        }
                    });
                }
            }
        }

        // Ajouter l'item résultant au centre
        addButton(new Button(38) { // Slot central
            @Override
            public ItemStack getItem() {
                return recipe.getResult();
            }

            @Override
            public void onClick(Player player) {
                // Ne rien faire lors du clic sur l'item résultant
            }
        });

        // Bouton retour
        addButton(new Button(44) { 
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ARROW, "§cRetour", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                // Retourner au menu précédent
                new ListRecipesMenu(module).displayTo(player);
            }
        });

        addButton(new Button(15) { // Slot en bas à gauche
            @Override
            public ItemStack getItem() {
                boolean isDisabled = !recipe.isDisableNaturalCraft();
                return ItemBuilder.of(
                    Material.CRAFTING_TABLE,
                    "§eBloque Craft Item de base : " + (isDisabled ? "§cDésactivé" : "§aActivé"),
                    Collections.singletonList("§7Cliquez pour " + (isDisabled ? "activer" : "désactiver"))
                ).make();
            }
        
            @Override
            public void onClick(Player player) {
                recipe.setDisableNaturalCraft(!recipe.isDisableNaturalCraft());
                craftManager.saveRecipes(); // Sauvegarder les modifications
                setupMenu(); // Rafraîchir le menu
                displayTo(player);
            }
        });
        
        // Bouton pour activer/désactiver les échanges avec les villageois
        addButton(new Button(25) { 
            @Override
            public ItemStack getItem() {
                boolean isDisabled = !recipe.isDisableVillagerTrade();
                return ItemBuilder.of(
                    Material.EMERALD,
                    "§eBloque Échanges Villageois : " + (isDisabled ? "§cDésactivé" : "§aActivé"),
                    Collections.singletonList("§7Cliquez pour " + (isDisabled ? "activer" : "désactiver"))
                ).make();
            }
        
            @Override
            public void onClick(Player player) {
                recipe.setDisableVillagerTrade(!recipe.isDisableVillagerTrade());
                craftManager.saveRecipes(); // Sauvegarder les modifications
                setupMenu(); // Rafraîchir le menu
                displayTo(player);
            }
        });
        // Bouton pour supprimer la recette
    addButton(new Button(41) { // Slot en bas au centre
        @Override
        public ItemStack getItem() {
            return ItemBuilder.of(
                Material.BARRIER,
                "§c§lSupprimer la recette",
                Collections.singletonList("§7Cliquez pour supprimer cette recette.")
            ).make();
        }

            @Override
            public void onClick(Player player) {
                // Supprimer la recette du manager
                craftManager.removeRecipe(recipe);

                // Sauvegarder les modifications dans le fichier JSON
                craftManager.saveRecipes();

                // Recharger les recettes depuis le fichier JSON
                craftManager.loadRecipes();

                // Fermer tous les menus ouverts
                player.closeInventory();

                // Envoyer un message de confirmation au joueur
                player.sendMessage("§aLa recette " + recipe.getResult().getItemMeta().getDisplayName() + " a été supprimée avec succès !");
            }
        });
    }
    

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, getSize(), getTitle());
        for (Button button : getButtons()) {
            inventory.setItem(button.getSlot(), button.getItem());
        }
        return inventory;
    }
}
