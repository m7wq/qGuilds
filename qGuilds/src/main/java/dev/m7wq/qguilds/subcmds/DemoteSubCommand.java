package dev.m7wq.qguilds.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.roles.RoleType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

public class DemoteSubCommand implements SubCommand {

    Set<String> canDemote = new HashSet<>(Plugin.getInstance().getConfigurationManager().getConfigures(ConfigurationManager.Configure.CAN_DEMOTE));

    @Override
    public void jobLoad(Player sender, String... arg) {

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

        Player target = Bukkit.getPlayerExact( arg[1]);

        if (!target.isOnline()){
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
        RoleType type = RoleType.getByName(promotedRole);

        membersManager.setRole(target.getName(), type);

        sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.PLAYER_DEMOTED)
                .replace("%player%",target.getName())
                .replace("%rank%",type.getDisplayRole()));


    }
}
