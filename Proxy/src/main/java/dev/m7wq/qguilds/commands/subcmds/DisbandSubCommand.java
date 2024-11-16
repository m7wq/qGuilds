package dev.m7wq.qguilds.commands.subcmds;

import java.util.HashSet;
import java.util.Set;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.enums.RoleType;

public class DisbandSubCommand implements SubCommand {
    
    Set<String> nameSet = new HashSet<>();

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if (arg.length != 1) {




            for (String message : PluginProxied.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.FAILED_DISBANDING)){
                sender.sendMessage(PluginProxied.getInstance().colorize(message.replace("%prefix%", PluginProxied.getInstance().getPrefix())));
            }
            return;
        }

        if (!PluginProxied.getInstance().getGuildsManager().onGuild(sender.getName())) {
            sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NOT_ON_GUILD_TO_DISBAND));
            return;
        }

         if (!PluginProxied.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
             sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));
             return;
         }




        sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.SUCCESS_DISBAND));

        PluginProxied.getInstance().getMySQL().removeGuild(PluginProxied.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
        nameSet.remove(sender.getName());

    }
}
