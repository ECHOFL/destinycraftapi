package abc.fliqq.auroramc.modules.customcraft;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }

        Player player = (Player) sender;

        // Créer une épée en diamant
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();

        if (meta != null) {
            // Ajouter un nom personnalisé
            meta.setDisplayName("§6Épée de Test");

            // Ajouter un AttributeModifier pour remplacer les attributs par défaut
            AttributeModifier damageModifier = new AttributeModifier(
                UUID.randomUUID(), "generic.attackDamage", 0, AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);

            // Ajouter le flag HIDE_ATTRIBUTES
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            // Appliquer les métadonnées à l'item
            sword.setItemMeta(meta);
        }

        // Donner l'item au joueur
        player.getInventory().addItem(sword);
        player.sendMessage("§aVous avez reçu une épée avec le flag HIDE_ATTRIBUTES.");

        return true;
    }
}