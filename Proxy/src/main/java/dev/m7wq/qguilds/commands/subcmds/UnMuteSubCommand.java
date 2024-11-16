package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;

public class UnMuteSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        HashSet<String> canUnMute = new HashSet<>(cfg.getConfigures(ConfigurationManager.Configure.CAN_UNMUTE));

        if (!canUnMute.contains(guildsManager.getPlayerRole(sender.getName()))){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }







        if (arg.length != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.SET_PLAYER);
            return;
        }
        String  targetName = arg[1];


        if (!guildsManager.onGuild(targetName,guildsManager.getPlayerGuild(sender.getName()).getGuildName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_YOUR_GUILD);
            return;
        }


        RoleType senderRole = RoleType.getByName(Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()));
        RoleType targetRole = RoleType.getByName(Plugin.getInstance().getGuildsManager().getPlayerRole(targetName));


        if (sender.getName().equalsIgnoreCase(targetName)){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_UNMUTE_YOURSELF);
            return;
        }

        if (targetRole.getPrestige() >= senderRole.getPrestige()) {
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_UNMUTE_HIGHER);
            return;
        }



        MembersManager membersManager = Plugin.getInstance().getMembersManager();

        if (!membersManager.isMuted(targetName))
        {
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_MUTED);
            return;
        }

        membersManager.setMuted(targetName,false);
        ChatUtil.sendMessage(sender, ConfigurationManager.Message.UNMUTED_SUCCESSFULLY);

        if (BungeeHook.isConnected(arg[1])) {
            ProxiedPlayer target = Plugin.getInstance().getProxy().getPlayer(arg[1]);
            ChatUtil.sendMessage(target, ConfigurationManager.Message.YOU_HAVE_BEEN_UNMUTED);

        }


    }
}
