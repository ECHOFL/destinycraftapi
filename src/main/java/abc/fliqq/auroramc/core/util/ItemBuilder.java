
package abc.fliqq.auroramc.core.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import abc.fliqq.auroramc.core.services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public static ItemBuilder of(Material material, String displayName, List<String> lore) {
        return new ItemBuilder(material)
                .setDisplayName(displayName)
                .setLore(lore);
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        meta.setDisplayName(MessageService.colorize(displayName));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(MessageService.colorize(line));
        }
        meta.setLore(coloredLore);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchant, int level) {
        meta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchants) {
        enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Ajoute un ou plusieurs ItemFlags à l'item.
     *
     * @param flags les ItemFlags à ajouter
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack make() {
        return build();
    }
}