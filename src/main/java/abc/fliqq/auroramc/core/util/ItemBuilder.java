
package abc.fliqq.auroramc.core.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import abc.fliqq.auroramc.core.services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utilitaire pour construire des ItemStack de manière fluide.
 * Fournit des méthodes permettant d'appliquer facilement le nom personnalisé,
 * le lore coloré, les enchantements et d'autres attributs.
 */
public class ItemBuilder {
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    /**
     * Construit un ItemBuilder pour le Material spécifié.
     *
     * @param material le Material de base pour l'ItemStack
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }
    
    /**
     * Méthode statique de commodité pour créer un ItemBuilder avec nom et lore prédéfinis.
     *
     * @param material le Material de base
     * @param displayName le nom personnalisé (les codes de couleur & seront traduits)
     * @param lore la liste des chaînes représentant le lore
     * @return une instance préconfigurée de ItemBuilder
     */
    public static ItemBuilder of(Material material, String displayName, List<String> lore) {
        return new ItemBuilder(material)
                .setDisplayName(displayName)
                .setLore(lore);
    }
    
    /**
     * Définit la quantité de l'item.
     *
     * @param amount la quantité souhaitée
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    /**
     * Définit le nom personnalisé de l'item avec coloration appliquée.
     *
     * @param displayName le nom personnalisé (utilisant les codes de couleur &)
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder setDisplayName(String displayName) {
        meta.setDisplayName(MessageService.colorize(displayName));
        return this;
    }
    
    /**
     * Définit le lore de l'item en appliquant la coloration à chaque ligne.
     *
     * @param lore une liste de chaînes représentant le lore
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder setLore(List<String> lore) {
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(MessageService.colorize(line));
        }
        meta.setLore(coloredLore);
        return this;
    }
    
    /**
     * Ajoute un enchantement à l'item.
     *
     * @param enchant l'enchantement à ajouter
     * @param level le niveau de l'enchantement
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder addEnchant(Enchantment enchant, int level) {
        meta.addEnchant(enchant, level, true);
        return this;
    }
    
    /**
     * Définit plusieurs enchantements à partir d'une Map.
     *
     * @param enchants une map contenant les enchantements et leur niveau
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchants) {
        enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        return this;
    }
    
    /**
     * Définit si l'item est incassable.
     *
     * @param unbreakable true pour rendre l'item incassable, false sinon
     * @return l'instance actuelle pour appel en chaîne
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }
    
    /**
     * Construit et retourne l'ItemStack configuré.
     *
     * @return l'ItemStack construit
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Alias de build() pour une syntaxe alternative.
     *
     * @return l'ItemStack construit
     */
    public ItemStack make() {
        return build();
    }
}
