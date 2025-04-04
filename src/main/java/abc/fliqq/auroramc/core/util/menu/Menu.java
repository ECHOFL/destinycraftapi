package abc.fliqq.auroramc.core.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import abc.fliqq.auroramc.modules.customcraft.CreationSession;
import abc.fliqq.auroramc.modules.customcraft.manager.CreationSessionManager;
import lombok.Getter;

public class Menu implements InventoryHolder{
    @Getter
    private final List<Button> buttons = new ArrayList<>();
    @Getter
    private int size = 9 * 3;
    @Getter
    private String title = "Menu";
    private Inventory inventory; // Stocker l'inventaire associé au menu


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
        // Créer l'inventaire avec `this` comme InventoryHolder
        inventory = Bukkit.createInventory(this, this.size, this.title);
        for (Button button : this.buttons) {
            inventory.setItem(button.getSlot(), button.getItem());
        }
        player.openInventory(inventory);
    
        // Mettre à jour le menu actuel dans la session si elle existe
        CreationSession session = CreationSessionManager.getSession(player);
        if (session != null) {
            session.setCurrentMenu(this);
        }
    
    }

    // Nouvelle méthode pour récupérer un bouton par son slot
    public Button getButton(int slot) {
        for (Button button : buttons) {
            if (button.getSlot() == slot) {
                return button;
            }
        }
        return null; 
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}