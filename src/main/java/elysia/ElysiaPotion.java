package elysia;


import elysia.command.CommandManager;
import elysia.command.CommandTabComplete;
import elysia.file.ConfigManager;
import elysia.file.FileListener;
import elysia.file.PlayerManager;
import elysia.file.PotionManager;
import elysia.hook.PlaceholderAPIHook;
import elysia.listener.DragonCoreEvent;
import elysia.listener.ElysiaPotionEvent;
import eos.moe.dragoncore.api.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public final class ElysiaPotion extends JavaPlugin {
    private static ElysiaPotion instance;
    private static ConfigManager configManager;
    private static PotionManager potionManager;
    private static PlayerManager playerManager;
    private static final HashMap<String,Boolean> dependManager = new HashMap<>();
    public static ElysiaPotion getInstance(){return instance;}
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static PotionManager getPotionManager() {
        return potionManager;
    }
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }
    public static HashMap<String, Boolean> getDependManager() {
        return dependManager;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        instance = this;
        createFile();
        configManager = ConfigManager.getInstance();
        potionManager = PotionManager.getInstance();
        playerManager = PlayerManager.getInstance();
        configManager.loadConfig();
        potionManager.loadPotionData();
        checkDepend();
        FileListener.startWatching(getDataFolder());
        Bukkit.getPluginCommand("ElysiaPotion").setExecutor(new CommandManager());
        Bukkit.getPluginCommand("ElysiaPotion").setTabCompleter(new CommandTabComplete());
        Bukkit.getPluginManager().registerEvents(new ElysiaPotionEvent(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
    }
    private void createFile() {
        saveDefaultConfig();
        createPotionFile();
    }
    private void checkDepend(){
        if (Bukkit.getPluginManager().getPlugin("AttributePlus") != null){
            getLogger().info("§a检查依赖 §7AttributePlus §a存在");
            dependManager.put("AttributePlus",true);
        } else {
            getLogger().info("§c检查依赖 §7AttributePlus §c不存在");
            dependManager.put("AttributePlus",false);
        }
        if (Bukkit.getPluginManager().getPlugin("DragonCore") != null){
            getLogger().info("§a检查依赖 §7DragonCore §a存在");
            dependManager.put("DragonCore",true);
            for (String key : configManager.getConfigData().getDragoncoreKeys().keySet())
                CoreAPI.registerKey(key);
            Bukkit.getPluginManager().registerEvents(new DragonCoreEvent(),this);
        } else {
            getLogger().info("§c检查依赖 §7DragonCore §c不存在");
            dependManager.put("DragonCore",false);
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            getLogger().info("§a检查依赖 §7PlaceholderAPI §a存在");
            dependManager.put("PlaceholderAPI",true);
            new PlaceholderAPIHook().register();
        } else {
            getLogger().info("§c检查依赖 §7PlaceholderAPI §c不存在");
            dependManager.put("PlaceholderAPI",false);
        }
        if (Bukkit.getPluginManager().getPlugin("SkillApi") != null){
            getLogger().info("§a检查依赖 §7SkillApi §a存在");
            dependManager.put("SkillApi",true);
        } else {
            getLogger().info("§c检查依赖 §7SkillApi §c不存在");
            dependManager.put("SkillApi",false);
        }
    }
    private void createPotionFile() {
        Path potionsFolderPath = getDataFolder().toPath().resolve("potions");
        createDirectoryIfNotExists(potionsFolderPath);
        Path potionsFilePath = potionsFolderPath.resolve("示例药水.yml");
        if (!Files.exists(potionsFilePath)) {
            try (InputStream resourceStream = getResourceAsStream()) {
                Files.copy(resourceStream, potionsFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create sample file.", e);
            }
        }
    }
    private void createDirectoryIfNotExists(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to create directory.", e);
            }
        }
    }
    private InputStream getResourceAsStream() {
        InputStream resourceStream = getResource("potions/示例药水.yml");
        if (resourceStream == null) {
            throw new RuntimeException("Resource " + "potions/示例药水.yml" + " not found in classpath.");
        }
        return resourceStream;
    }
}
