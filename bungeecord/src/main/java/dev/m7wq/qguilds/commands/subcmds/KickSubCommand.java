package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;

import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;

public class KickSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {


        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        HashSet<String> canKick = new HashSet<>(
                RolesUtil.getPermissionRoles(sender.getName(), Permission.can_kick)
        );



        if (!canKick.contains(guildsManager.getPlayerRole(sender.getName()).getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;

        }


        if (arg.length != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.SET_PLAYER);
            return;
        }


        String targetName = arg[1];


        Plugin.getInstance().getMembersManager().kick(targetName);
        ChatUtil.sendMessage(sender, ConfigurationManager.Message.KICKED_SUCCESSFULLY);


        if (BungeeHook.isConnected(arg[1])){
            ProxiedPlayer target = Plugin.getInstance().getProxy().getPlayer(arg[1]);
            ChatUtil.sendMessage(target, ConfigurationManager.Message.YOU_HAVE_BEEN_KICKED);

        }

        BungeeHook.callEvent(new ChangeHappenEvent(null,targetName));

    }
}
