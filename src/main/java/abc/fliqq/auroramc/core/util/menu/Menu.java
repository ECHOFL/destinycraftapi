package abc.fliqq.auroramc.core.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import abc.fliqq.auroramc.AuroraAPI;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.manager.CreationSessionManager;
import lombok.Getter;

public class Menu {
    @Getter
    private final List<Button> buttons = new ArrayList<>();
    private int size = 9 * 3;
    private String title = "Menu";

    protected final void addButton(Button button) {
        buttons.add(button);
    }

    protected final void setSize(int size) {
        this.size = size;
    }

    protected final void setTitle(String title) {
        this.title = title;
    }

    public final void displayTo(Player player) {
        Inventory inventory = Bukkit.createInventory(player, this.size, this.title);
        for (Button button : this.buttons) {
            inventory.setItem(button.getSlot(), button.getItem());
        }
        player.openInventory(inventory);

        // Mettre à jour le menu actuel dans la session
        CreationSession session = CreationSessionManager.getSession(player);
        if (session != null) {
            session.setCurrentMenu(this);
        }

        // Journal pour vérifier que le menu est affiché
        LoggerUtil.info("Menu '" + this.title + "' affiché pour le joueur : " + player.getName());
    }

    // Nouvelle méthode pour récupérer un bouton par son slot
    public Button getButton(int slot) {
        for (Button button : buttons) {
            if (button.getSlot() == slot) {
                return button;
            }
        }
        return null; // Aucun bouton trouvé pour ce slot
    }
}