package elysia.hook;

import elysia.ElysiaPotion;
import elysia.file.data.PotionData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    /*
    %ElysiaPotion_cooldown_id%:指定name的药水冷却时间
    %ElysiaPotion_currentCooldown_id%:指定name的药水剩余冷却时间
    %ElysiaPotion_useTime%:创建hud时获取当前使用药水的使用时间
     */
    @Override
    public String getIdentifier() {
        return "ElysiaPotion";
    }
    @Override
    public String getAuthor() {
        return "Elysia";
    }
    @Override
    public String getVersion() {
        return ElysiaPotion.getInstance().getDescription().getVersion();
    }
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null || params == null || params.isEmpty()) return null;
        if (params.startsWith("cooldown")) return getCooldown(params.split("_")[1]);
        if (params.startsWith("currentCooldown")) return getCurrentCooldown(player,params.split("_")[1]);
        if (params.startsWith("useTime")) return getUseTime(player);
        return null;
    }
    private String getCooldown(String params){
        for (PotionData potionData : ElysiaPotion.getPotionManager().getPotionDataHashMap().values()){
            if (potionData.getName().equals(params)){
                return String.valueOf((long)potionData.getCooldown());
            }
        }
        return null;
    }
    private String getCurrentCooldown(Player player,String params){
        for (PotionData potionData : ElysiaPotion.getPotionManager().getPotionDataHashMap().values()){
            if (potionData.getName().equals(params)) {
                String id  = potionData.getId();
                long useTime = ElysiaPotion.getPlayerManager().getPotionUseTime(player.getUniqueId(),id);
                if (useTime == 0L) return null;
                long now = System.currentTimeMillis();
                int cooldown = ElysiaPotion.getPotionManager().getPotionDataHashMap().get(id).getCooldown();
                return String.valueOf(((useTime + cooldown * 1000L) > now) ? (useTime + cooldown * 1000L - now) : 0);
            }
        }
        return null;
    }
    private String getUseTime(Player player){
        return ElysiaPotion.getPlayerManager().getPlayerDragonUse(player.getUniqueId());
    }
}
