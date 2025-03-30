package abc.fliqq.auroramc.core.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.units.qual.s;

public class Menu {
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
    public final void displayTo(Player player){
        Inventory inventory = Bukkit.createInventory(player, this.size, this.title);
        for(Button button : this.buttons){
            inventory.setItem(button.getSlot(), button.getItem());
        }
    }
}
