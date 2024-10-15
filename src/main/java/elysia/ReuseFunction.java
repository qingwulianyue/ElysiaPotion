package elysia;

import elysia.file.data.PotionData;
import elysia.hook.AttributePlusHook;
import elysia.hook.BukkitHook;
import elysia.hook.ProSkillApiHook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReuseFunction {
    public static boolean tryUsePotion(PotionData potionData, Player player) {
        if (ElysiaPotion.getPlayerManager().checkPotionInCooldown(player.getUniqueId(),potionData.getId())) {
            player.sendMessage(ElysiaPotion.getConfigManager().getConfigData().getPrefix()
                    .replaceAll("&","§")+
                    ElysiaPotion.getConfigManager().getConfigData().getMessages().get("onCooldown")
                            .replaceAll("%potion%",potionData.getName())
                            .replaceAll("%cooldown%", String.valueOf(potionData.getCooldown() - (System.currentTimeMillis() - ElysiaPotion.getPlayerManager().getPotionUseTime(player.getUniqueId(),potionData.getId()))/1000L))
                            .replaceAll("&","§"));
            return false;
        }
        if (potionData.getUseTime() != 0){
            ElysiaPotion.getPlayerManager().setPlayerDragonUse(player.getUniqueId(),potionData.getUseTime());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"core openhud " + player.getName() + " 提示文本1");
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (potionData.getEffects() != null) ReuseFunction.enableEffects(player,potionData);
                    if (potionData.getPotions() != null) ReuseFunction.enablePotions(player,potionData);
                    if (potionData.getAttributes() != null) ReuseFunction.enableAttribute(player,potionData);
                }
            }.runTaskLater(ElysiaPotion.getInstance(),potionData.getUseTime() * 20L);
        } else {
            if (potionData.getEffects() != null) ReuseFunction.enableEffects(player,potionData);
            if (potionData.getPotions() != null) ReuseFunction.enablePotions(player,potionData);
            if (potionData.getAttributes() != null) ReuseFunction.enableAttribute(player,potionData);
        }
        player.sendMessage(ElysiaPotion.getConfigManager().getConfigData().getPrefix()
                .replaceAll("&","§") +
                ElysiaPotion.getConfigManager().getConfigData().getMessages().get("usePotion")
                        .replaceAll("%player%",player.getDisplayName())
                        .replaceAll("%potion%",potionData.getName())
                        .replaceAll("&","§"));
        if (potionData.getGroup() == null) ElysiaPotion.getPlayerManager().refreshPotionUseTime(player.getUniqueId(),potionData.getId());
        String group = potionData.getGroup();
        List<PotionData> potionDataList = ElysiaPotion.getPotionManager().getPotionGroupDataHashMap().get(group);
        for (PotionData potionData1 : potionDataList) ElysiaPotion.getPlayerManager().refreshPotionUseTime(player.getUniqueId(),potionData1.getId());
        return potionData.getConsume();
    }
    public static PotionData searchPotionByName(String name){
        if (name == null) return null;
        HashMap<String, PotionData> potionDataHashMap = ElysiaPotion.getPotionManager().getPotionDataHashMap();
        for (PotionData potionData : potionDataHashMap.values()){
            if (potionData.getName().contains(name))
                return potionData;
        }
        return null;
    }
    public static void enableEffects(Player player,PotionData potionData){
        HashMap<String,String> effects = potionData.getEffects();
        if (effects.containsKey("health")) enableHealth(player,effects.get("health"));
        if (effects.containsKey("mana") && ElysiaPotion.getDependManager().get("SkillApi")) enableMana(player,effects.get("mana"));
        if (effects.containsKey("hunger")) enableHunger(player,effects.get("hunger"));
    }
    public static void enablePotions(Player player,PotionData potionData){
        HashMap<String ,String > potions = potionData.getPotions();
        for (String potion : potions.keySet()){
            String[] split = potions.get(potion).split(":");
            int level = Integer.parseInt(split[0]);
            int duration = Integer.parseInt(split[1]);
            BukkitHook.addPotion(player,potion,level,duration);
        }
    }
    public static void enableAttribute(Player player,PotionData potionData){
        if (!ElysiaPotion.getDependManager().get("AttributePlus")) return;
        List<String> attributes = potionData.getAttributes();
        List<String> newAttributes = parseAttributes(player,attributes);
        AttributePlusHook.addAttribute(player,newAttributes, potionData.getId());
        if (potionData.getAttributeContinue() != -1)
            takeAttributeTask(player,potionData.getId(), potionData.getAttributeContinue());
    }
    private static void takeAttributeTask(Player player,String key,long time){
        new BukkitRunnable(){
            @Override
            public void run() {
                AttributePlusHook.takeAttribute(player,key);
            }
        }.runTaskLater(ElysiaPotion.getInstance(),time * 20L);
    }
    private static List<String> parseAttributes(Player player,List<String> attributes){
        List<String> newAttributes = new ArrayList<>();
        for (String attribute : attributes){
            String[] strings = attribute.split(":");
            if (strings[1].startsWith("%") && strings[1].endsWith("%"))
                strings[1] = PlaceholderAPI.setPlaceholders(player,strings[1]);
            attribute = strings[0] + strings[1];
            newAttributes.add(attribute);
        }
        return newAttributes;
    }
    private static void enableHealth(Player player,String string){
        String[] split = string.split(":");
        double value = Double.parseDouble(split[0]);
        long time = Long.parseLong(split[1]) * 20L;
        new BukkitRunnable(){
            long count = 0L;
            @Override
            public void run() {
                if (!player.isOnline()) this.cancel();
                if (count < time){
                    BukkitHook.addHealth(player,value);
                    count += 20L;
                } else this.cancel();
            }
        }.runTaskTimer(ElysiaPotion.getInstance(),0L,20L);
    }
    private static void enableMana(Player player,String string){
        String[] split = string.split(":");
        double value = Double.parseDouble(split[0]);
        long time = Long.parseLong(split[1]) * 20L;
        new BukkitRunnable(){
            long count = 0L;
            @Override
            public void run() {
                if (count < time){
                    if (!player.isOnline()) this.cancel();
                    ProSkillApiHook.addMana(player,value);
                    count += 20L;
                } else this.cancel();
            }
        }.runTaskTimer(ElysiaPotion.getInstance(),0L,time);
    }
    private static void enableHunger(Player player,String string){
        String[] split = string.split(":");
        int value = Integer.parseInt(split[0]);
        long time = Long.parseLong(split[1]) * 20L;
        new BukkitRunnable(){
            long count = 0L;
            @Override
            public void run() {
                if (!player.isOnline()) this.cancel();
                if (count < time){
                    BukkitHook.addHunger(player,value);
                    count += 20L;
                } else this.cancel();
            }
        }.runTaskTimer(ElysiaPotion.getInstance(),0L,time);
    }
}
