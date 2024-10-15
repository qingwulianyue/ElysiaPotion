package elysia.hook;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import org.bukkit.entity.Player;

public class ProSkillApiHook {
    public static void addMana(Player player,double value){
        if (player == null) return;
        PlayerData playerData = SkillAPI.getPlayerData(player.getUniqueId());
        playerData.giveMana(value);
    }
}
