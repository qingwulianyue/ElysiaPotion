package elysia.listener;

import elysia.ReuseFunction;
import elysia.file.data.PotionData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ElysiaPotionEvent implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;
        ItemStack itemStack = event.getItem();
        PotionData potionData = ReuseFunction.searchPotionByName(itemStack.getItemMeta().getDisplayName());
        if (potionData == null) return;
        Player player = event.getPlayer();
        if (!ReuseFunction.tryUsePotion(potionData, player)) return;
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.getInventory().setItemInMainHand(itemStack);
    }
}
