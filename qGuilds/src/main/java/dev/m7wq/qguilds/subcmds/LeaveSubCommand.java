package dev.m7wq.qguilds.subcmds;

import org.bukkit.entity.Player;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.Commandable;
import dev.m7wq.qguilds.managers.ConfigurationManager.Message;
import dev.m7wq.qguilds.roles.RoleType;

public class LeaveSubCommand implements Commandable{

    @Override
    public void jobLoad(Player sender, String... arg) {
        if(!Plugin.getInstance().getGuildsManager().onGuild(sender.getName())){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(Message.ARENT_ON_GUILD_TO_LEAVE));
            return;
        
        }   

        if(Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(Message.YOU_ARE_MASTER));
            return;
        }

        Plugin.getInstance().getGuildsManager().removePlayer(sender.getName());
        sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(Message.YOU_LEFT_GUILD));

        
    }

}
