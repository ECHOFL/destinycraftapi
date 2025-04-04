package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class EnchantMenu extends Menu {
    private final CreationSession session;

    public EnchantMenu(CreationSession session) {
        this.session = session;
        setTitle("§aGestion des enchantements");
        setSize(27);

        // Ajouter un bouton pour chaque enchantement valide
        int slot = 0;
        for (Enchantment enchantment : Enchantment.values()) {
            // Vérifier si l'enchantement est applicable à l'item de base
            if (!enchantment.canEnchantItem(session.getBaseItem())) {
                continue; // Ignorer les enchantements non valides
            }

            if (slot >= 27) break; // Limiter à 27 enchantements (taille du menu)

            addButton(new Button(slot++) {
                @Override
                public ItemStack getItem() {
                    int currentLevel = session.getEnchantments().getOrDefault(enchantment, 0);
                    return ItemBuilder.of(Material.ENCHANTED_BOOK, "§e" + enchantment.getKey().getKey(),
                            Collections.singletonList("§7Niveau actuel : §a" + currentLevel))
                            .setEnchants(currentLevel > 0 ? Collections.singletonMap(Enchantment.EFFICIENCY, 1) : Collections.emptyMap()) // Glow si niveau > 0
                            .make();
                }

                @Override
                public void onClick(Player player) {
                    onClick(player, ClickType.LEFT);
                }
                @Override
                public void onClick(Player player, ClickType clickType) {
                    handleEnchantmentClick(player, enchantment, clickType);
                }
            });
        }

        // Bouton retour
        addButton(new Button(26) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ARROW, "§cRetour", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                new CreationMenu(session).displayTo(player);
            }
        });
    }

    /**
     * Gère les clics sur les boutons d'enchantement.
     *
     * @param player      Le joueur qui a cliqué.
     * @param enchantment L'enchantement concerné.
     * @param clickType   Le type de clic (gauche ou droit).
     */
    private void handleEnchantmentClick(Player player, Enchantment enchantment, ClickType clickType) {
        int currentLevel = session.getEnchantments().getOrDefault(enchantment, 0);

        if (clickType.isLeftClick()) {
            // Augmenter le niveau
            session.getEnchantments().put(enchantment, currentLevel + 1);
        } else if (clickType.isRightClick()) {
            // Diminuer le niveau
            if (currentLevel > 1) {
                session.getEnchantments().put(enchantment, currentLevel - 1);
            } else {
                session.getEnchantments().remove(enchantment); // Supprimer si le niveau atteint 0
            }
        }

        // Mettre à jour l'aperçu de l'item
        session.updateBaseItem();

        // Rafraîchir le menu
        new EnchantMenu(session).displayTo(player);
    }
}