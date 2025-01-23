package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.roles.RolesManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

public class PromoteSubCommand implements SubCommand {



    @Override
    public void jobLoad(ProxiedPlayer sender, String... args) {
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();
        final Set<String> canPromote = new HashSet<>(
                RolesUtil.getPermissionRoles(sender.getName(), Permission.can_promote)
        );
        Role role = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName());


        if (!canPromote.contains(role.getName())) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.NO_PERMISSION));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.SET_PLAYER));
            return;
        }



        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();



        String target = args[1];

        if(!guildsManager.onGuild(target,guildsManager.getPlayerGuild(sender.getName()).getGuildName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_YOUR_GUILD);
            return;
        }




        if (target.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE_YOURSELF));
            return;
        }

        RolesManager manager = Plugin.getInstance().getRolesManager();




        if (RolesUtil.isLowerThanTargetOrEqual(sender,target)) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE_HIGHER_ROLE_PLAYER));
            return;
        }

        Role newRole = manager.promoteMember(target);



        if (newRole == null) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ON_HIGHEST_RANK));
            return;
        }



        int senderRoleLength = RolesUtil.getRoleLength(sender.getName(),
                Plugin.getInstance().getMembersManager().getMember(sender.getName()).getRole());

        if (RolesUtil.getRoleLength(target,newRole) >= senderRoleLength) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE));
            return;
        }


        manager.setRole(target, newRole);
        sender.sendMessage(Plugin.getInstance().colorize(
                messages.getMessage(sender, ConfigurationManager.Message.PLAYER_PROMOTED)
                        .replace("%rank%", newRole.getColor()+newRole.getName())
                        .replace("%player%", target)
        ));

        BungeeHook.callEvent(new ChangeHappenEvent(null,target));
    }
}
