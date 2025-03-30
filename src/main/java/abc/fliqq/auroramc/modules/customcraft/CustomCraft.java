package abc.fliqq.auroramc.modules.customcraft;

import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * Représente une recette de craft personnalisée.
 * La recette est définie par :
 * - Un résultat (ItemStack)
 * - Une forme (tableau de 3 à 9 chaînes représentant les lignes du craft)
 * - Un mapping entre un caractère et l'ItemStack représentant l'ingrédient associé
 */
public class CustomCraft {
    
    private final ItemStack result;
    private final String[] shape;
    private final Map<Character, ItemStack> ingredients;

    /**
     * Construit une nouvelle recette custom.
     *
     * @param result le résultat de la crafterie
     * @param shape le tableau de chaînes représentant la forme (ex. {"ABC", "DEF", "GHI"})
     * @param ingredients le mapping entre symbole et ingrédient
     */
    public CustomCraft(ItemStack result, String[] shape, Map<Character, ItemStack> ingredients) {
        this.result = result;
        this.shape = shape;
        this.ingredients = ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public String[] getShape() {
        return shape;
    }

    public Map<Character, ItemStack> getIngredients() {
        return ingredients;
    }

    /**
     * Convertit cette recette custom en une ShapedRecipe de Bukkit et l'enregistre dans le serveur.
     * 
     * @param key la key unique pour la recette
     * @param plugin le plugin qui enregistre la recette
     * @return la recette enregistrée
     */
    public Recipe register(NamespacedKey key, Plugin plugin) {
        ShapedRecipe bukkitRecipe = new ShapedRecipe(key, result);
        bukkitRecipe.shape(shape);
        
        // Parcourir les ingrédients et les assigner à la recette
        ingredients.forEach((symbol, ingredient) -> {
            bukkitRecipe.setIngredient(symbol, ingredient.getType());
        });
        
        // Enregistrement de la recette via l'API de Bukkit
        plugin.getServer().addRecipe(bukkitRecipe);
        return bukkitRecipe;
    }

    /**
     * Méthode utilitaire pour vérifier si une grille donnée correspond à cette recette.
     * Ici, grid représente une matrice bidimensionnelle d'ItemStack correspondant à la grille de craft.
     * Vous pouvez adapter cette logique en fonction de votre besoin.
     *
     * @param grid une matrice 2D d'ItemStack représentant la grille de craft
     * @return true si la grille correspond à la recette, false sinon.
     */
    public boolean matches(ItemStack[][] grid) {
        // Vérifier d'abord la dimension de la grille (par exemple 3x3)
        if (grid.length != shape.length) {
            return false;
        }
        for (int row = 0; row < shape.length; row++) {
            if (grid[row].length != shape[row].length()) {
                return false;
            }
            for (int col = 0; col < shape[row].length(); col++) {
                char expectedSymbol = shape[row].charAt(col);
                ItemStack expectedIngredient = ingredients.get(expectedSymbol);
                ItemStack providedItem = grid[row][col];

                // Si le symbole est un espace, considérer que la case doit être vide
                if (expectedSymbol == ' ') {
                    if (providedItem != null && providedItem.getType().isSolid()) {
                        return false;
                    }
                } else {
                    // Vérifier que l'ingrédient attendu n'est pas null et correspond au type de l'item fourni.
                    if (expectedIngredient == null || providedItem == null || 
                        !providedItem.getType().equals(expectedIngredient.getType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
