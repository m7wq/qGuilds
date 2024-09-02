package dev.m7wq.qguilds.proxy.commands.subcmds;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.interfaces.SubCommand;
import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;
import dev.m7wq.qguilds.proxy.managers.GuildsManager;
import dev.m7wq.qguilds.proxy.managers.MembersManager;
import dev.m7wq.qguilds.proxy.enums.RoleType;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.HashSet;
import java.util.Set;

public class DemoteSubCommand implements SubCommand {

    Set<String> canDemote = new HashSet<>(Plugin.getInstance().getConfigurationManager().getConfigures(ConfigurationManager.Configure.CAN_DEMOTE));

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        if (arg.length != 2){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.SET_PLAYER));
            return;
        }

        GuildsManager manager = Plugin.getInstance().getGuildsManager();

        String senderName = sender.getName();
       String playerRoleName =  manager.getPlayerRole(senderName);

       if (!canDemote.contains(playerRoleName)){
           sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));

           return;
       }

        ProxiedPlayer target = Plugin.getInstance().getProxy().getPlayer( arg[1]);

        if (!target.isConnected()){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.PLAYER_OFFLINE));
            return;
        }

        RoleType targetRoleType = RoleType.getByName(manager.getPlayerRole(target.getName()));
        RoleType senderRoleType = RoleType.getByName(playerRoleName);

        if (targetRoleType.getPrestige() >= senderRoleType.getPrestige()){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_DEMOTE));
            return;
        }

        MembersManager membersManager = Plugin.getInstance().getMembersManager();

        String promotedRole = membersManager.demotePlayer(target.getName());

        if (promotedRole.isEmpty()){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_LOWEST_ROLE));
            return;
        }

        RoleType type = RoleType.getByName(promotedRole);

        membersManager.setRole(target.getName(), type);

        sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.PLAYER_DEMOTED)
                .replace("%player%",target.getName())
                .replace("%rank%",type.getDisplayRole()));


    }
}
