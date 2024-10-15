package elysia.hook;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitHook {
    public static void addHunger(Player player,int value){
        int now = player.getFoodLevel();
        player.setFoodLevel(now + value);
    }
    public static void addHealth(Player player,double value){
        double now = player.getHealth();
        double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.setHealth(Math.min((now + value), max));
    }
    public static void addPotion(Player player,String effect,int level,int duration){
        PotionEffectType effectType = PotionEffectType.getByName(effect);
        PotionEffect potionEffect = new PotionEffect(effectType,duration * 20,level);
        player.addPotionEffect(potionEffect);
    }
}
