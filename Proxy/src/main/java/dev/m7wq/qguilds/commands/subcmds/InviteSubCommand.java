package dev.m7wq.qguilds.commands.subcmds;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Invitation;
import dev.m7wq.qguilds.utils.ChatUtil;
import static dev.m7wq.qguilds.utils.ChatUtil.sendPlayerIsOffline;

public class InviteSubCommand implements SubCommand {



    Set<String> validRolesToInvite = new HashSet<>(
            Plugin.getInstance().getConfigurationManager().getConfigures(
                    ConfigurationManager.Configure.CAN_INVITE
            )
    );

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager messagesConfig = Plugin.getInstance().getConfigurationManager();
        InvitingManager invitingManager = Plugin.getInstance().getInvitingManager();


        if (!validRolesToInvite.contains(manager.getPlayerRole(sender.getName()))){
            sender.sendMessage(messagesConfig.getMessage(sender,ConfigurationManager.Message.NO_PERMISSION));
            return;
        }

        if (arg.length > 2){
            for (String string : Plugin.getInstance().getConfigurationManager().getMessages(sender,ConfigurationManager.Message.FAILED_INVITING)){
                sender.sendMessage(Plugin.getInstance().colorize(string.replace("%prefix%", Plugin.getInstance().getPrefix())));
            }
            return;
        }

        if (arg.length < 2){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.SET_PLAYER));
            return;
        }

        String targetName = arg[1];


        if (!BungeeHook.isConnected(arg[1])) {
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.PLAYER_OFFLINE));
            return;
        }

        ProxiedPlayer target = Plugin.getInstance().getProxy().getPlayer(arg[1]);





        Guild senderGuild = manager.getPlayerGuild(sender.getName());

        if (target.getName().equalsIgnoreCase(sender.getName())){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.CANT_INVITE_YOURSELF));
            return;
        }
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        if(guildsManager.getPlayerGuild(targetName).equals(guildsManager.getPlayerGuild(sender.getName()))){
            sender.sendMessage(messagesConfig.getMessage(sender,ConfigurationManager.Message.ALREADY_IN_YOUR_GUILD));
            return;
        }


        if (invitingManager.getInvitedMap().containsKey(target.getName())
                && invitingManager.isInvitedByTheClan(target,senderGuild.getGuildName())){
            sender.sendMessage(messagesConfig.getMessage(sender,ConfigurationManager.Message.ALREADY_INVITED));
            return;
        }

        for (String string : messagesConfig.getMessages(sender,ConfigurationManager.Message.INVITE_MESSAGE)){
            if (string.contains("%yes%")||string.contains("%no%")) {
                ChatUtil.sendSections(target,senderGuild.getGuildName(), ConfigurationManager.Message.INVITE_MESSAGE);
                continue;
            }
            target.sendMessage(Plugin.getInstance().colorize(string.replace("%prefix%", Plugin.getInstance().getPrefix())                            .replace("%inviter%",sender.getName())
                    .replace("%guild_display_name%",senderGuild.getDisplayName())));
        }

        long expireTime = Plugin.getInstance().getConfigurationManager().getConfig().getLong("options.expire-time");

        for (String string : messagesConfig.getMessages(sender,ConfigurationManager.Message.INVITED_SUCCESSFULLY)){

            string = string.replace("%prefix%", Plugin.getInstance().getPrefix())
                            .replace("%player%",targetName)
                                    .replace("%time%",String.valueOf(expireTime));

            sender.sendMessage(Plugin.getInstance().colorize(string));
        }

        Invitation invitation = new Invitation(senderGuild,sender.getName());

        invitingManager.getInvitedMap().get(targetName).add(invitation);

        ProxyServer.getInstance().getScheduler().schedule(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {

                        if (invitingManager.getInvitedMap().get(targetName).contains(invitation)) {
                            invitingManager.getInvitedMap().get(targetName).remove(invitation);
                            target.sendMessage(messagesConfig.getMessage(sender,ConfigurationManager.Message.EXPIRED_INVITE).replace("%guild_display_name%",senderGuild.getDisplayName()));
                            invitingManager.getInvitedMap().get(targetName).remove(invitation);
                        }


            }
        }, expireTime, TimeUnit.SECONDS);


    }


}
