package dev.m7wq.qguilds.proxy.commands.subcmds;

import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.interfaces.SubCommand;
import dev.m7wq.qguilds.proxy.enums.RoleType;

public class LeaveSubCommand implements SubCommand {

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if(!Plugin.getInstance().getGuildsManager().onGuild(sender.getName())){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.ARENT_ON_GUILD_TO_LEAVE));
            return;
        
        }   

        if(Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.YOU_ARE_MASTER));
            return;
        }

        Plugin.getInstance().getMembersManager().removePlayer(sender.getName());
        sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.YOU_LEFT_GUILD));

        
    }

}
