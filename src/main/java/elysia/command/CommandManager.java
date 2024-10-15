package elysia.command;

import elysia.ElysiaPotion;
import elysia.command.subcommands.HelpCommand;
import elysia.command.subcommands.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings == null) return false;
        if (strings.length == 0)
            commandSender.sendMessage(
                    ElysiaPotion.getConfigManager().getConfigData().getPrefix() +
                            "请输入/ElysiaPotion help 以获取指令帮助"
            );
        switch (strings[0].toLowerCase()){
            case "help" : new HelpCommand().execute(commandSender,strings);break;
            case "reload" : new ReloadCommand().execute(commandSender,strings);break;
        }
        return true;
    }
}
