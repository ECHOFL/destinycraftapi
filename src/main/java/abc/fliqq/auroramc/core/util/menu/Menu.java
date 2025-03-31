package abc.fliqq.auroramc.core.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.checkerframework.checker.units.qual.s;

import abc.fliqq.auroramc.AuroraAPI;
import lombok.Getter;

public class Menu {
    @Getter
    private final List<Button> buttons = new ArrayList<>();
    private int size = 9*3;
    private String title = "Menu";

    protected final void addButton(Button button) {
        buttons.add(button);
    }

    protected final void setSize(int size){
        this.size=size;
    }
    protected final void setTitle(String title){
        this.title=title;
    }
    public final void displayTo(Player player) {
        Inventory inventory = Bukkit.createInventory(player, this.size, this.title);
        for (Button button : this.buttons) {
            inventory.setItem(button.getSlot(), button.getItem());
        }
        player.setMetadata("AuroraAPI", new FixedMetadataValue(AuroraAPI.getInstance(), this));
        player.openInventory(inventory);
    
        // Journal pour vérifier l'association
        AuroraAPI.getInstance().getLogger().info("Menu affiché pour le joueur : " + player.getName());
    }
}
