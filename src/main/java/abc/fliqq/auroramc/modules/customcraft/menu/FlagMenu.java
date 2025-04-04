package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class FlagMenu extends Menu {
    private final CreationSession session;

    public FlagMenu(CreationSession session) {
        this.session = session;
        setTitle("§aGestion des flags");
        setSize(27);

        // Bouton pour "HIDE_ENCHANTS"
        addButton(new Button(10) { // Slot 10
            @Override
            public ItemStack getItem() {
                boolean isActive = session.getFlags().contains(ItemFlag.HIDE_ENCHANTS);
                return ItemBuilder.of(Material.ENCHANTED_BOOK, "§eMasquer les enchantements",
                        Collections.singletonList(isActive ? "§aActivé" : "§cDésactivé"))
                        .setEnchants(isActive ? Collections.singletonMap(org.bukkit.enchantments.Enchantment.EFFICIENCY, 1) : Collections.emptyMap()) // Glow si activé
                        .addItemFlags(ItemFlag.HIDE_ENCHANTS) // Masquer les enchantements visibles
                        .make();
            }

            @Override
            public void onClick(Player player) {
                // Activer ou désactiver le flag
                if (session.getFlags().contains(ItemFlag.HIDE_ENCHANTS)) {
                    session.getFlags().remove(ItemFlag.HIDE_ENCHANTS);
                } else {
                    session.getFlags().add(ItemFlag.HIDE_ENCHANTS);
                }

                session.updateBaseItem(); // Mettre à jour l'item de base
                new FlagMenu(session).displayTo(player); // Rafraîchir le menu
            }
        });

        addButton(new Button(12) { // Slot 12
            @Override
            public ItemStack getItem() {
                boolean isActive = session.getFlags().contains(ItemFlag.HIDE_ATTRIBUTES);
                LoggerUtil.info("HIDE_ATTRIBUTES est " + (isActive ? "activé" : "désactivé") + " pour le joueur : " + session.getPlayer().getName());
                return ItemBuilder.of(Material.IRON_SWORD, "§eMasquer les attributs",
                        Collections.singletonList(isActive ? "§aActivé" : "§cDésactivé"))
                        .setEnchants(isActive ? Collections.singletonMap(org.bukkit.enchantments.Enchantment.EFFICIENCY, 1) : Collections.emptyMap()) // Glow si activé
                        .addItemFlags(ItemFlag.HIDE_ENCHANTS) // Masquer les enchantements visibles
                        .make();
            }

            @Override
            public void onClick(Player player) {
                // Activer ou désactiver le flag
                if (session.getFlags().contains(ItemFlag.HIDE_ATTRIBUTES)) {
                    session.getFlags().remove(ItemFlag.HIDE_ATTRIBUTES);
                    LoggerUtil.info("HIDE_ATTRIBUTES retiré pour le joueur : " + player.getName());
                } else {
                    session.getFlags().add(ItemFlag.HIDE_ATTRIBUTES);
                    LoggerUtil.info("HIDE_ATTRIBUTES ajouté pour le joueur : " + player.getName());
                }

                session.updateBaseItem(); // Mettre à jour l'item de base
                new FlagMenu(session).displayTo(player); // Rafraîchir le menu
            }
        });

        // Bouton pour "Unbreakable"
        addButton(new Button(14) { // Slot 14
            @Override
            public ItemStack getItem() {
                boolean isUnbreakable = session.isUnbreakable();
                return ItemBuilder.of(Material.NETHERITE_INGOT, "§eUnbreakable",
                        Collections.singletonList(isUnbreakable ? "§aActivé" : "§cDésactivé"))
                        .make();
            }

            @Override
            public void onClick(Player player) {
                // Inverser l'état "Unbreakable"
                session.setUnbreakable(!session.isUnbreakable());
                session.updateBaseItem(); // Mettre à jour l'item de base
                new FlagMenu(session).displayTo(player); // Rafraîchir le menu
            }
        });

        // Bouton retour
        addButton(new Button(26) { // Slot 26
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.ARROW, "§cRetour", Collections.emptyList()).make();
            }

            @Override
            public void onClick(Player player) {
                new CreationMenu(session).displayTo(player);
            }
        });
        // Bouton pour "HIDE_UNBREAKABLE"
    addButton(new Button(16) { // Slot 16
        @Override
        public ItemStack getItem() {
            boolean isActive = session.getFlags().contains(ItemFlag.HIDE_UNBREAKABLE);
            return ItemBuilder.of(Material.DIAMOND_CHESTPLATE, "§eMasquer l'état incassable",
                    Collections.singletonList(isActive ? "§aActivé" : "§cDésactivé"))
                    .setEnchants(isActive ? Collections.singletonMap(org.bukkit.enchantments.Enchantment.EFFICIENCY, 1) : Collections.emptyMap()) // Glow si activé
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS) // Masquer les enchantements visibles
                    .make();
        }

        @Override
        public void onClick(Player player) {
            // Activer ou désactiver le flag
            if (session.getFlags().contains(ItemFlag.HIDE_UNBREAKABLE)) {
                session.getFlags().remove(ItemFlag.HIDE_UNBREAKABLE);
            } else {
                session.getFlags().add(ItemFlag.HIDE_UNBREAKABLE);
            }

            session.updateBaseItem(); // Mettre à jour l'item de base
            new FlagMenu(session).displayTo(player); // Rafraîchir le menu
        }
    });
    }

    
}