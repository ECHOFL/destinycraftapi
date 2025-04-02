package abc.fliqq.auroramc.modules.customcraft;

import java.util.*;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import abc.fliqq.auroramc.core.util.menu.Menu;

public class CreationSession {
    private final Player player;
    private final ItemStack baseItem;
    private Menu currentMenu; // Nouveau champ pour suivre le menu actuel

    private String name;
    private final List<String> lore = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final Set<ItemFlag> flags = new HashSet<>();
    private boolean unbreakable = false;
    private final String[] shape = new String[]{"   ", "   ", "   "};
    private final Map<Character, ItemStack> ingredients = new HashMap<>();

    public enum InputType {
        NAME, LORE
    }
    private InputType awaitingInput;

    public CreationSession(Player player, ItemStack baseItem) {
        this.player = player;
        this.baseItem = baseItem.clone(); // Clone pour éviter les modifications accidentelles
        this.name = baseItem.getItemMeta() != null ? baseItem.getItemMeta().getDisplayName() : null;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public String getName() {
        return name;
    }
    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void addLoreLine(String line) {
        if (lore.size() < 10) { // Limite de 10 lignes
            lore.add(line);
        }
    }

    public void removeLoreLine(int index) {
        if (index >= 0 && index < lore.size()) {
            lore.remove(index);
        }
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void toggleEnchantment(Enchantment enchantment) {
        enchantments.put(enchantment, enchantments.getOrDefault(enchantment, 0) + 1);
    }

    public Set<ItemFlag> getFlags() {
        return flags;
    }

    public void toggleFlag(ItemFlag flag) {
        if (flags.contains(flag)) {
            flags.remove(flag);
        } else {
            flags.add(flag);
        }
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public String[] getShape() {
        return shape;
    }

    public void setCraftSlot(int row, int col, ItemStack item) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            char key = (char) ('A' + row * 3 + col); // Génère un caractère unique pour chaque slot
            shape[row] = shape[row].substring(0, col) + key + shape[row].substring(col + 1);
            ingredients.put(key, item.clone());
        }
    }

    public void clearCraftSlot(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            char key = shape[row].charAt(col);
            shape[row] = shape[row].substring(0, col) + " " + shape[row].substring(col + 1);
            ingredients.remove(key);
        }
    }

    public Map<Character, ItemStack> getIngredients() {
        return ingredients;
    }

    public CustomCraft toCustomCraft() {
        return new CustomCraft(baseItem, shape, ingredients, unbreakable);
    }

    public void updateBaseItem(){
        if(baseItem == null) return;
        ItemStack updatedItem = baseItem.clone();
        ItemMeta meta = updatedItem.getItemMeta(); 

        if(meta != null ){
            // Appliquer le nom
            if (name != null && !name.isEmpty()) {
                meta.setDisplayName(name);
            }

            // Appliquer le lore
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }

            // Appliquer les enchantements
            meta.getEnchants().forEach((enchantment, level) -> meta.removeEnchant(enchantment)); // Supprimer les anciens enchantements
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));

            // Appliquer les flags
            meta.getItemFlags().forEach(meta::removeItemFlags); // Supprimer les anciens flags
            flags.forEach(meta::addItemFlags);

            // Définir l'indestructibilité
            meta.setUnbreakable(unbreakable);

            updatedItem.setItemMeta(meta);
            }

            // Mettre à jour l'aperçu de l'item
            baseItem.setItemMeta(updatedItem.getItemMeta());
    }

    public InputType getAwaitingInput() {
        return awaitingInput;
    }

    public void setAwaitingInput(InputType awaitingInput) {
        this.awaitingInput = awaitingInput;
    }
}