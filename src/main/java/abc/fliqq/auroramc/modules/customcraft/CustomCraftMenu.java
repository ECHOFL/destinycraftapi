package abc.fliqq.auroramc.modules.customcraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.Message;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.ItemBuilder;
import abc.fliqq.auroramc.core.util.menu.Button;
import abc.fliqq.auroramc.core.util.menu.Menu;

public class CustomCraftMenu extends Menu{
    
    private final CustomCraftManager customCraftManager;

    public CustomCraftMenu(CustomCraftModule module) {
        this.setTitle(MessageService.colorize("&aMenu recettes personnalisées"));

        customCraftManager = module.getCustomCraftManager();
        this.addButton(new Button(12) {
            @Override
            public ItemStack getItem() {
                return ItemBuilder.of(Material.GOLDEN_APPLE, "Affiche les recettes personnalisées", null).make();
            }

            @Override
            public void onClick(Player player) {
                new ListRecipesMenu().displayTo(player);
            }
        });
    }

    private class ListRecipesMenu extends Menu{
        public ListRecipesMenu(){
            this.setSize(9*6);
            this.setTitle(MessageService.colorize("&bRecettes actives"));
            int startingSlot = 0;
            for(CustomCraft craft : customCraftManager.getRecipes()){

                if(startingSlot++ > 9*6) return;

                this.addButton(new Button(startingSlot++) {

                @Override
                public ItemStack getItem() {
                    return ItemBuilder.of(craft.getResult().getType(), craft.getResult().getItemMeta().getDisplayName(), craft.getResult().getItemMeta().getLore()).make();
                }

                @Override
                public void onClick(Player player) {
                    // ouvre nouveau menu avec le craft
                }
                
               }); 
            }
        }
    }
}
