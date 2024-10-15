package elysia.file;

import elysia.ElysiaPotion;
import elysia.file.data.ConfigData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();
    public static ConfigManager getInstance(){return instance;}
    private ElysiaPotion Instance;
    private ConfigData configData;
    private ConfigManager(){}
    public ConfigData getConfigData() {
        return configData;
    }
    public void loadConfig(){
        configData = null;
        Instance = ElysiaPotion.getInstance();
        Instance.reloadConfig();
        FileConfiguration config = Instance.getConfig();
        HashMap<String,String> dragoncoreKeys = new HashMap<>();
        for (String keys : config.getConfigurationSection("dragoncoreKeys").getKeys(false)){
            String value = config.getString("dragoncoreKeys." + keys);
            dragoncoreKeys.put(keys,value);
        }
        HashMap<String,String> messages = new HashMap<>();
        messages.put("reload",config.getString("messages.reload"));
        messages.put("usePotion",config.getString("messages.usePotion"));
        messages.put("onCooldown",config.getString("messages.onCooldown"));
        configData = new ConfigData(
                config.getBoolean("debug"),
                config.getString("prefix"),
                dragoncoreKeys,
                messages
        );
        logConfigIfDebug();
    }
    private void logConfigIfDebug(){
        if (!configData.isDebug())
            return;
        Instance.getLogger().info("§6----------------------------");
        Instance.getLogger().info("§a debug模式: " + configData.isDebug());
        Instance.getLogger().info("§a 消息前缀: " + configData.getPrefix());
        Instance.getLogger().info("§a 龙核按键:");
        HashMap<String,String> dragoncoreKeys = configData.getDragoncoreKeys();
        for (String keys : dragoncoreKeys.keySet()){
            Instance.getLogger().info("§a 按键名称: " + keys);
            Instance.getLogger().info("§a 槽位名称: " + dragoncoreKeys.get(keys));
            Instance.getLogger().info("§6-------------");
        }
    }
}
