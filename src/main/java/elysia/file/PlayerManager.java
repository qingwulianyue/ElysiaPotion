package elysia.file;

import elysia.ElysiaPotion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private PlayerManager(){}
    private static HashMap<UUID,HashMap<String,Long>> playerUseTimeHashMap = new HashMap<>();
    private static HashMap<UUID,String> playerDragonUseTimeHashMap = new HashMap<>();
    private static final PlayerManager instance = new PlayerManager();
    public static PlayerManager getInstance(){return instance;}

    public void refreshPotionUseTime(UUID uuid,String key){
        HashMap<String,Long> potionUseTimeHashMap;
        if (playerUseTimeHashMap.containsKey(uuid) && playerUseTimeHashMap.get(uuid) != null && !playerUseTimeHashMap.get(uuid).isEmpty())
            potionUseTimeHashMap = playerUseTimeHashMap.get(uuid);
        else potionUseTimeHashMap = new HashMap<>();
        potionUseTimeHashMap.put(key,System.currentTimeMillis());
        playerUseTimeHashMap.put(uuid,potionUseTimeHashMap);
    }
    public boolean checkPotionInCooldown(UUID uuid,String key){
        if (!playerUseTimeHashMap.containsKey(uuid)) return false;
        HashMap<String,Long> potionUseTimeHashMap = playerUseTimeHashMap.get(uuid);
        if (!potionUseTimeHashMap.containsKey(key)) return false;
        long useTime = potionUseTimeHashMap.get(key);
        int cooldown = ElysiaPotion.getPotionManager().getPotionDataHashMap().get(key).getCooldown();
        long now = System.currentTimeMillis();
        return useTime + cooldown * 1000L > now;
    }
    public Long getPotionUseTime(UUID uuid,String key){
        if (!playerUseTimeHashMap.containsKey(uuid)) return 0L;
        if (!playerUseTimeHashMap.get(uuid).containsKey(key)) return 0L;
        return playerUseTimeHashMap.get(uuid).get(key);
    }

    public void setPlayerDragonUse(UUID uuid,int useTime) {
        playerDragonUseTimeHashMap.put(uuid, String.valueOf(useTime));
    }
    public String getPlayerDragonUse(UUID uuid) {
        return playerDragonUseTimeHashMap.get(uuid);
    }
}
