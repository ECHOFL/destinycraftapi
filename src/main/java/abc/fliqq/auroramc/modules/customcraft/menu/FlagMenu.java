package abc.fliqq.auroramc.modules.customcraft.menu;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;

import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;

public class FlagMenu extends Menu {
    private final CreationSession session;

    public FlagMenu(CreationSession session) {
        this.session = session;
        setTitle("§aGestion des flags");
        setSize(27);

        // Ajouter un bouton pour chaque flag disponible
        int slot = 0;
        for (ItemFlag flag : ItemFlag.values()) {
            if (slot >= 27) break; // Limiter à 27 flags (taille du menu)

            addButton(new Button(slot++) {
                @Override
                public ItemStack getItem() {
                    boolean isActive = session.getFlags().contains(flag);

                    return ItemBuilder.of(Material.BARRIER, "§e" + flag.name(),
                            Collections.singletonList(isActive ? "§aActivé" : "§cDésactivé"))
                            .setEnchants(isActive ? Collections.singletonMap(org.bukkit.enchantments.Enchantment.UNBREAKING, 1) : Collections.emptyMap()) // Glow si activé
                            .make();
                }

                @Override
                public void onClick(Player player) {
                    // Activer ou désactiver le flag
                    if (session.getFlags().contains(flag)) {
                        session.getFlags().remove(flag);
                    } else {
                        session.getFlags().add(flag);
                    }

                    // Mettre à jour l'aperçu de l'item
                    session.updateBaseItem();

                    // Rafraîchir le menu
                    new FlagMenu(session).displayTo(player);
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
}