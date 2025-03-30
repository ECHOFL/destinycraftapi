package abc.fliqq.auroramc.core.util.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public abstract class Button {
    @Getter
    private final int slot;
    
    public Button(int slot) {
        this.slot = slot;
    }
    public abstract ItemStack getItem();
    public abstract void onClick(Player player);
}
