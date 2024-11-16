package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

public class DemoteSubCommand implements SubCommand {

    private final Set<String> canDemote = new HashSet<>(Plugin.getInstance()
            .getConfigurationManager().getConfigures(ConfigurationManager.Configure.CAN_DEMOTE));

    @Override
    public void jobLoad(ProxiedPlayer sender, String... args) {
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();



        String senderName = sender.getName();
        String senderRoleName = guildsManager.getPlayerRole(senderName);

        if (!canDemote.contains(senderRoleName)) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.NO_PERMISSION));
            return;
        }


        if (args.length != 2) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.SET_PLAYER));
            return;
        }





        String target = args[1];

        if(!guildsManager.onGuild(target,guildsManager.getPlayerGuild(senderName).getGuildName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_YOUR_GUILD);
            return;
        }




        RoleType targetRoleType = RoleType.getByName(guildsManager.getPlayerRole(target));
        RoleType senderRoleType = RoleType.getByName(senderRoleName);

        if (targetRoleType == null || senderRoleType == null) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_LOWEST_ROLE));
            return;
        }


        if (targetRoleType == RoleType.MEMBER) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_LOWEST_ROLE));
            return;
        }

        if (target.equalsIgnoreCase(sender.getName())){

            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_DEMOTE_YOURSELF);
            return;
        }

        if (targetRoleType.getPrestige() >= senderRoleType.getPrestige()) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_DEMOTE));
            return;
        }

        MembersManager membersManager = Plugin.getInstance().getMembersManager();
        String demotedRole = membersManager.demotePlayer(target);

        RoleType newRole = RoleType.getByName(demotedRole);
        if (newRole != null) {
            membersManager.setRole(target, newRole);

            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.PLAYER_DEMOTED)
                    .replace("%player%", target)
                    .replace("%rank%", newRole.getDisplayRole()));
        } else {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_LOWEST_ROLE));
        }

        BungeeHook.callEvent(new ChangeHappenEvent(null,target));
    }
}
