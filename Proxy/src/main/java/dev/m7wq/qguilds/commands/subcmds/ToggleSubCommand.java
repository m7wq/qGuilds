package dev.m7wq.qguilds.commands.subcmds;

import java.util.Set;

import dev.m7wq.qguilds.managers.ConfigurationManager;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class ToggleSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        if (arg.length != 2 || !arg[1].equalsIgnoreCase("on") && !"off".equalsIgnoreCase(arg[1])){
            messages.getMessages(sender,ConfigurationManager.Message.NOT_VALID_DATA).forEach(message -> {
                sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%", Plugin.getInstance().getPrefix())));
            });
            return;
        }
        
        

        Set<String> currentSet = ChatUtil.chatToggleSet;

        if (arg[1].equalsIgnoreCase("on")){
            if(currentSet.contains(sender.getName()))
            {
                sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.ALREADY_ON));
                return;
            }

            currentSet.add(sender.getName());
            sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.ENABLED_TOGGLE));
            return;

        }

        if (arg[1].equalsIgnoreCase("off")) {

            if (!currentSet.contains(sender.getName())) {
                sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_OFF));
                return;
            }

            currentSet.remove(sender.getName());
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.DISABLED_TOGGLE));
        }
    }
}
