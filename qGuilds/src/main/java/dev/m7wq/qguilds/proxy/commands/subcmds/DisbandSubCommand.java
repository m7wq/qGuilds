package dev.m7wq.qguilds.proxy.commands.subcmds;

import java.util.HashSet;
import java.util.Set;

import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.interfaces.SubCommand;
import dev.m7wq.qguilds.proxy.enums.RoleType;

public class DisbandSubCommand implements SubCommand {
    
    Set<String> nameSet = new HashSet<>();

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if (arg.length != 1) {




            for (String message : Plugin.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.FAILED_DISBANDING)){
                sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%",Plugin.getInstance().getPrefix())));
            }
            return;
        }

        if (!Plugin.getInstance().getGuildsManager().onGuild(sender.getName())) {
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NOT_ON_GUILD_TO_DISBAND));
            return;
        }

         if (!Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
             sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));
             return;
         }




        sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.SUCCESS_DISBAND));

        Plugin.getInstance().getMySQL().removeGuild(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
        nameSet.remove(sender.getName());

    }
}
