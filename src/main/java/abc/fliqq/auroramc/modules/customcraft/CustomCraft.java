
package abc.fliqq.auroramc.modules.customcraft;

import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class CustomCraft {
    @Getter
    private final String id;
    private final ItemStack result;
    private final String[] shape;
    private final Map<Character, ItemStack> ingredients;
    private final boolean unbreakable; // Nouveau champ
    @Getter
    @Setter
    private boolean disableNaturalCraft = false; 
    @Getter
    @Setter
    private boolean disableVillagerTrade = false;
    
    public CustomCraft(ItemStack result, String[] shape, Map<Character, ItemStack> ingredients, boolean unbreakable) {
        this.result = result;
        this.id= UUID.randomUUID().toString();
        this.shape = shape;
        this.ingredients = ingredients;
        this.unbreakable = unbreakable;
    }

    public ItemStack getResult() {
        return result.clone(); // Retourner un clone pour éviter les modifications accidentelles
    }

    public String[] getShape() {
        return shape;
    }

    public Map<Character, ItemStack> getIngredients() {
        return ingredients;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CustomCraft other = (CustomCraft) obj;

        // Comparer les résultats (type, nom, lore, enchantements, unbreakable)
        if (!result.getType().equals(other.result.getType())) return false;

        if (result.hasItemMeta() != other.result.hasItemMeta()) return false;

        if (result.hasItemMeta()) {
            if (result.getItemMeta().hasDisplayName() != other.result.getItemMeta().hasDisplayName()) return false;
            if (result.getItemMeta().hasDisplayName() && !result.getItemMeta().getDisplayName().equals(other.result.getItemMeta().getDisplayName())) return false;

            if (result.getItemMeta().hasLore() != other.result.getItemMeta().hasLore()) return false;
            if (result.getItemMeta().hasLore() && !result.getItemMeta().getLore().equals(other.result.getItemMeta().getLore())) return false;

            if (!result.getItemMeta().getEnchants().equals(other.result.getItemMeta().getEnchants())) return false;

            if (result.getItemMeta().isUnbreakable() != other.result.getItemMeta().isUnbreakable()) return false;
        }

        // Comparer les formes
        if (shape.length != other.shape.length) return false;
        for (int i = 0; i < shape.length; i++) {
            if (!shape[i].equals(other.shape[i])) return false;
        }

        // Comparer les ingrédients
        return ingredients.equals(other.ingredients);
    }

    @Override
    public int hashCode() {
        int result = this.result.hashCode();
        result = 31 * result + java.util.Arrays.hashCode(shape);
        result = 31 * result + ingredients.hashCode();
        result = 31 * result + Boolean.hashCode(unbreakable);
        return result;
    }
}
