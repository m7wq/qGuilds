package dev.m7wq.qguilds.commands.subcmds.editRoles;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.commands.subcmds.editRoles.utils.DisplayUtil;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.utils.RolesUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class EditRolesSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        List<String> canEditRoles = RolesUtil.getPermissionRoles(sender.getName(), Permission.can_editRoles);


        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();


        String senderName = sender.getName();
        String senderRoleName = guildsManager.getPlayerRole(senderName).getName();

        if (!canEditRoles.contains(senderRoleName)) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.NO_PERMISSION));
            return;
        }


        DisplayUtil.sendRolesList(sender);
    }
}
