package elysia.command.subcommands;

import elysia.ElysiaPotion;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    public void execute(CommandSender commandSender, String[] strings){
        if (strings.length != 1) return;
        ElysiaPotion.getPotionManager().loadPotionData();
        ElysiaPotion.getConfigManager().loadConfig();
        commandSender.sendMessage(ElysiaPotion.getConfigManager().getConfigData().getMessages().get("reload").replaceAll("&","§"));
    }
}
