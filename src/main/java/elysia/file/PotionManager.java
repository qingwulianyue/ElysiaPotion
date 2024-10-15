package elysia.file;

import elysia.ElysiaPotion;
import elysia.file.data.ConfigData;
import elysia.file.data.PotionData;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PotionManager {
    private static final PotionManager instance = new PotionManager();
    public static PotionManager getInstance(){return instance;}
    private ElysiaPotion Instance;
    private final HashMap<String,PotionData> potionDataHashMap = new HashMap<>();
    private final HashMap<String,List<PotionData>> potionGroupDataHashMap = new HashMap<>();
    private PotionManager(){}
    public HashMap<String,PotionData> getPotionDataHashMap() {
        return potionDataHashMap;
    }
    public HashMap<String, List<PotionData>> getPotionGroupDataHashMap() {
        return potionGroupDataHashMap;
    }

    public void loadPotionData(){
        Instance = ElysiaPotion.getInstance();
        potionDataHashMap.clear();
        File file = new File(Instance.getDataFolder() + "/potions");
        findAllYmlFiles(file);
    }
    private void findAllYmlFiles(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                // 如果是文件夹则递归查找
                findAllYmlFiles(file);
            } else if (file.getName().endsWith(".yml")) {
                // 如果是YML文件则加入结果列表
                File potionFile = new File(folder.toString() + "/" + file.getName());
                YamlConfiguration config = YamlConfiguration.loadConfiguration(potionFile);
                loadAllData(config);
            }
        }
    }
    private void loadAllData(YamlConfiguration config) {
        for (String firstKey : config.getKeys(false)){
            HashMap<String,String> effects = tryGetEffects(config,firstKey);
            HashMap<String,String> potions = tryGetPotions(config,firstKey);
            PotionData potionData = new PotionData(
                    firstKey,
                    config.getString(firstKey + ".name"),
                    config.getInt(firstKey + ".cooldown"),
                    config.getInt(firstKey + ".useTime"),
                    config.getConfigurationSection(firstKey).contains("group")?config.getString(firstKey + ".group"):null,
                    effects,
                    potions,
                    config.getConfigurationSection(firstKey).contains("attributes")?config.getStringList(firstKey + ".attributes"):null,
                    config.getConfigurationSection(firstKey).contains("attributeContinue")?config.getInt(firstKey + ".attributeContinue"):null,
                    config.getBoolean(firstKey + ".consume")
            );
            potionDataHashMap.put(firstKey,potionData);
            loadGroupPotion(potionData);
            logPotionDataIfDebug(potionData);
        }
    }
    private HashMap<String,String> tryGetEffects(YamlConfiguration config,String key){
        HashMap<String,String> effects = new HashMap<>();
        if (!config.getConfigurationSection(key).contains("effects"))
            return null;
        for (String effectsKeys : config.getConfigurationSection(key + ".effects").getKeys(false)){
            effects.put(effectsKeys,config.getString(key + ".effects." + effectsKeys));
        }
        return effects;
    }
    private HashMap<String,String> tryGetPotions(YamlConfiguration config,String key){
        HashMap<String,String> potions = new HashMap<>();
        if (!config.getConfigurationSection(key).contains("potions"))
            return null;
        for (String potionsKeys : config.getConfigurationSection(key + ".potions").getKeys(false)){
            potions.put(potionsKeys,config.getString(key + ".potions." + potionsKeys));
        }
        return potions;
    }
    private void loadGroupPotion(PotionData potionData){
        if (potionData.getGroup() == null)
            return;
        String group = potionData.getGroup();
        List<PotionData> potionDataList;
        if (potionDataHashMap.containsKey(group)) potionDataList = potionGroupDataHashMap.get(group);
        else potionDataList = new ArrayList<>();
        potionDataList.add(potionData);
        potionGroupDataHashMap.put(group,potionDataList);
    }
    private void logPotionDataIfDebug(PotionData potionData){
        if (ElysiaPotion.getConfigManager().getConfigData().isDebug()){
            Instance.getLogger().info("§a 载入药水配置: " + potionData.getId());
            Instance.getLogger().info("§a 药水名称: " + potionData.getName());
            Instance.getLogger().info("§a 药水冷却时间: " + potionData.getCooldown());
            Instance.getLogger().info("§a 药水使用时间: " + potionData.getUseTime());
            Instance.getLogger().info("§a 药水组: " + potionData.getGroup());
            Instance.getLogger().info("§a 药水基础效果: " + potionData.getEffects());
            Instance.getLogger().info("§a 药水原版效果: " + potionData.getPotions());
            Instance.getLogger().info("§a 药水属性效果: " + potionData.getAttributes());
            Instance.getLogger().info("§a 药水属性持续时间: " + potionData.getAttributeContinue());
            Instance.getLogger().info("§a 药水是否消耗: " + potionData.getConsume());
            Instance.getLogger().info("§6-------------");
        }
    }
}
