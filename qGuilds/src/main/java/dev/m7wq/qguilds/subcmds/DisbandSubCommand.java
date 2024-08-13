package dev.m7wq.qguilds.subcmds;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.Commandable;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.ConfigurationManager.Message;
import dev.m7wq.qguilds.roles.RoleType;

public class DisbandSubCommand implements Commandable {
    
    Set<String> nameSet = new HashSet<>();

    @Override
    public void jobLoad(Player sender, String... arg) {
        if (arg.length != 1) {

            for (String message : Plugin.getInstance().getConfigurationManager().getMessages(ConfigurationManager.Message.FAILED_DISBANDING)){
                sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%",Plugin.getInstance().getPrefix())));
            }
            return;
        }

        if (!Plugin.getInstance().getGuildsManager().onGuild(sender.getName())) {
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(ConfigurationManager.Message.NOT_ON_GUILD_TO_DISBAND));
            return;
        }

         if (!Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase(RoleType.MASTER.name())){
             sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(ConfigurationManager.Message.NO_PERMISSION));
             return;
         }




        sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(Message.SUCCESS_DISBAND));

        Plugin.getInstance().getMySQL().removeGuild(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
        nameSet.remove(sender.getName());

    }
}
