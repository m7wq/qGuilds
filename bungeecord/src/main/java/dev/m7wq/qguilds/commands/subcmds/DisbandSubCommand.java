package dev.m7wq.qguilds.commands.subcmds;

import java.util.HashSet;
import java.util.Set;

import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;

public class DisbandSubCommand implements SubCommand {
    
    Set<String> nameSet = new HashSet<>();

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if (arg.length != 1) {

            if (!Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).getName().equalsIgnoreCase(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getHighestRole().getName())){
                sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));
                return;
            }


            for (String message : Plugin.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.FAILED_DISBANDING)){
                sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%", Plugin.getInstance().getPrefix())));
            }
            return;
        }

        if (!Plugin.getInstance().getGuildsManager().onGuild(sender.getName())) {
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NOT_ON_GUILD_TO_DISBAND));
            return;
        }






        sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.SUCCESS_DISBAND));

        Plugin.getInstance().getCooldownManager().guildCooldown.remove(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
        Plugin.getInstance().getCooldownManager().ownerCooldown.remove(sender.getName());
        Plugin.getInstance().getGuildsManager().getGuildMap().remove(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
//       Plugin.getInstance().getMySQL().removeGuild(Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName()).getGuildName());
        nameSet.remove(sender.getName());

        BungeeHook.callEvent(new ChangeHappenEvent(sender,null));

    }
}
