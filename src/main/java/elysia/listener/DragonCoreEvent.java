package elysia.listener;

import elysia.ElysiaPotion;
import elysia.ReuseFunction;
import elysia.file.data.PotionData;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.KeyPressEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DragonCoreEvent implements Listener {
    @EventHandler
    public void onKeyPress(KeyPressEvent event){
        String pressKey = event.getKey();
        HashMap<String,String> dragoncoreKey = ElysiaPotion.getConfigManager().getConfigData().getDragoncoreKeys();
        if (!dragoncoreKey.containsKey(pressKey)) return;
        String slotName = dragoncoreKey.get(pressKey);
        if (!Config.getSlotConfig().contains(slotName)) return;
        Player player = event.getPlayer();
        SlotAPI.getSlotItem(player, slotName, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || itemStack.getType() == Material.AIR) return;
                PotionData potionData = ReuseFunction.searchPotionByName(itemStack.getItemMeta().getDisplayName());
                if (potionData == null) return;
                if (!ReuseFunction.tryUsePotion(potionData, player)) return;
                itemStack.setAmount(itemStack.getAmount() - 1);
                SlotAPI.setSlotItem(player,slotName,itemStack,true);
            }
            @Override
            public void onFail() {
                player.sendMessage(ElysiaPotion.getConfigManager().getConfigData().getPrefix() +
                        "对应槽位没有物品");
            }
        });
    }
}
