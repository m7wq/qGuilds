package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.roles.RolesManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class DemoteSubCommand implements SubCommand {



    @Override
    public void jobLoad(ProxiedPlayer sender, String... args) {
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        List<String> canDemote = RolesUtil.getPermissionRoles(sender.getName(), Permission.can_demote);





        String senderName = sender.getName();
        String senderRoleName = guildsManager.getPlayerRole(senderName).getName();

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




        if (target.equalsIgnoreCase(sender.getName())){

            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_DEMOTE_YOURSELF);
            return;
        }

        if (RolesUtil.isLowerThanTargetOrEqual(sender,target)) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_DEMOTE));
            return;
        }

        RolesManager manager = Plugin.getInstance().getRolesManager();

        Role newRole = manager.promoteMember(sender.getName());

        if (newRole != null) {


            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.PLAYER_DEMOTED)
                    .replace("%player%", target)
                    .replace("%rank%", newRole.getColor()+newRole.getName()));
        } else {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ALREADY_LOWEST_ROLE));
        }

        BungeeHook.callEvent(new ChangeHappenEvent(null,target));
    }
}
