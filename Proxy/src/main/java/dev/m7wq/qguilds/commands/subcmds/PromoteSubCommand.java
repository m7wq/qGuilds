package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.HashSet;
import java.util.Set;

public class PromoteSubCommand implements SubCommand {

    Set<String> canPromote = new HashSet<>(
            PluginProxied.getInstance().getConfigurationManager().getConfigures(ConfigurationManager.Configure.CAN_PROMOTE)
    );

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        ConfigurationManager messages = PluginProxied.getInstance().getConfigurationManager();

        if (arg.length != 2){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.SET_PLAYER));
            return;
        }
        String role = PluginProxied.getInstance().getGuildsManager().getPlayerRole(sender.getName());

        if (!canPromote.contains(role)){
            sender.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));
            return;
        }

        ProxiedPlayer target = PluginProxied.getInstance().getProxy().getPlayer(arg[1]);



        if (!target.isConnected()){
            ChatUtil.sendPlayerIsOffline(sender);
            return;
        }

        if (target.getName().equalsIgnoreCase(sender.getName())){
            sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.CANT_PROMOTE_YOURSELF));
            return;
        }

        MembersManager manager = PluginProxied.getInstance().getMembersManager();

        RoleType senderRole = RoleType.getByName(role);
        RoleType targetRole = RoleType.getByName(PluginProxied.getInstance().getGuildsManager().getPlayerRole(target.getName()));

        if (targetRole.getPrestige() >= senderRole.getPrestige()){
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE_HIGHER_ROLE_PLAYER));
            return;
        }

        String promotedRole = manager.promotePlayer(target.getName());

        RoleType roleType = RoleType.getByName(promotedRole);



        if (roleType.getPrestige() >= senderRole.getPrestige()){
            PluginProxied.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE);
            return;
        }

        manager.setRole(target.getName(), roleType);

        sender.sendMessage(PluginProxied.getInstance().colorize( messages.getMessage(sender,ConfigurationManager.Message.PLAYER_PROMOTED).replace("%rank%",roleType.getDisplayRole()).replace("%player%",target.getName())));
    }
}
