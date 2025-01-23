package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.roles.RolesManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TransferSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        MembersManager membersManager = Plugin.getInstance().getMembersManager();
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager configurationManager = Plugin.getInstance().getConfigurationManager();


        Role senderRole = guildsManager.getPlayerRole(sender.getName());
        Guild senderGuild = Plugin.getInstance().getGuildsManager().getPlayerGuild(sender.getName());

        if (senderRole!=senderGuild.getHighestRole()){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }

        if (arg.length != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.SET_PLAYER);
            return;
        }

        if (BungeeHook.isConnected(arg[1])) {
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.PLAYER_OFFLINE));
            return;
        }

        ProxiedPlayer target = Plugin.getInstance().getProxy().getPlayer(arg[1]);

        if (target.getName().equalsIgnoreCase(sender.getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_TRANSFER_TO_YOURSELF);
            return;
        }





        Role afterTransferRole = senderGuild.getRoles().get(1);

        membersManager.setRole(sender.getName(),afterTransferRole);
        membersManager.setRole(target.getName(),senderGuild.getHighestRole());

        ChatUtil.sendMessage(sender, ConfigurationManager.Message.SUCCESSFULLY_TRANSFERRED);
        target.sendMessage(Plugin.getInstance().colorize( configurationManager.getMessage(target, ConfigurationManager.Message.YOU_ARE_NOW_OWNER).replace("%guild%",guildsManager.getPlayerGuild(sender.getName()).getGuildName())));

        BungeeHook.callEvent(new ChangeHappenEvent(null,sender.getName()));

    }
}
