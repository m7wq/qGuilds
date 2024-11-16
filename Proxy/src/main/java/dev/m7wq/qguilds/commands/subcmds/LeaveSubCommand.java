package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.enums.RoleType;

public class LeaveSubCommand implements SubCommand {

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if(!PluginProxied.getInstance().getGuildsManager().onGuild(sender.getName())){
            sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.ARENT_ON_GUILD_TO_LEAVE));
            return;
        
        }   

        if(PluginProxied.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
            sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.YOU_ARE_MASTER));
            return;
        }

        PluginProxied.getInstance().getMembersManager().removePlayer(sender.getName());
        sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.YOU_LEFT_GUILD));

        
    }

}
