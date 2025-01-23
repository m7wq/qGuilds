package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.utils.RolesUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.HashSet;
import java.util.List;

public class MyPermissionsSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        List<String> message = Plugin.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.PERMISSIONS_MESSAGE);


        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();

        String role = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).getName();

        List<String> canInvite,canMute,canUnMute,canKick,canPromote,canDemote,canSetTag,canSetTagColor,canRename,canEditRoles;

        String ign = sender.getName();

        canInvite = RolesUtil.getPermissionRoles(ign, Permission.can_invite);
        canMute = RolesUtil.getPermissionRoles(ign,Permission.can_mute);
        canUnMute = RolesUtil.getPermissionRoles(ign,Permission.can_unMute);
        canKick = RolesUtil.getPermissionRoles(ign,Permission.can_kick);
        canPromote = RolesUtil.getPermissionRoles(ign,Permission.can_promote);
        canDemote = RolesUtil.getPermissionRoles(ign,Permission.can_demote);
        canSetTag = RolesUtil.getPermissionRoles(ign,Permission.can_setTag);
        canSetTagColor = RolesUtil.getPermissionRoles(ign,Permission.can_setTagColor);
        canRename = RolesUtil.getPermissionRoles(ign,Permission.can_rename);
        canEditRoles = RolesUtil.getPermissionRoles(ign,Permission.can_editRoles);


        for (String string : message){
            string = string.replace("%invite%",doneMark(role,canInvite))
                    .replace("%mute%",doneMark(role,canMute))
                    .replace("%unmute%",doneMark(role,canUnMute))
                    .replace("%kick%",doneMark(role,canKick))
                    .replace("%promote%",doneMark(role,canPromote))
                    .replace("%demote%",doneMark(role,canDemote))
                    .replace("%set-tag%",doneMark(role,canSetTag))
                    .replace("%set-tag-color%",doneMark(role,canSetTagColor))
                    .replace("%rename%",doneMark(role,canRename))
                    .replace("%editRoles%",doneMark(role,canEditRoles))
                            .replace("%prefix%",Plugin.getInstance().getPrefix());

            sender.sendMessage(Plugin.getInstance().colorize(string));
        }

    }

    String doneMark(String role, List<String> allowedRoles){
        return getMark(allowedRoles.contains(role));
    }

    String getMark(boolean bool){
        if (bool){
            return Plugin.getInstance().getConfigurationManager().getMessagesConfig().getString("permission.have");
        }else {
            return Plugin.getInstance().getConfigurationManager().getMessagesConfig().getString("permission.doesnt-have");
        }
    }
}
