package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.objects.Invitation;

public class IgnoreSubCommand implements SubCommand {

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        InvitingManager manager = PluginProxied.getInstance().getInvitingManager();
        ConfigurationManager messages = PluginProxied.getInstance().getConfigurationManager();



        if (arg.length != 2){
            messages.getMessages(sender,ConfigurationManager.Message.FAILED_IGNORING)
                    .forEach(message -> sender.sendMessage(PluginProxied.getInstance().colorize(
                            message.replace("%prefix%", PluginProxied.getInstance().getPrefix())
                    )));
            return;
        }

        String guildName = arg[1];

        if (!manager.isInvitedByTheClan(sender,guildName)){
            sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.NO_INVITES_RECEIVED));
            return;
        }

        InvitingManager invitingManager = PluginProxied.getInstance().getInvitingManager();

        @NotNull Invitation invitation =  invitingManager.getInvitedMap().get(sender.getName())
                .stream().filter(invitation1 -> invitation1.getGuild().getGuildName().equalsIgnoreCase(guildName))
                .findFirst().get();

        invitingManager.getInvitedMap().get(sender.getName()).remove(invitation);

        ProxiedPlayer inviter = PluginProxied.getInstance().getProxy().getPlayer(invitation.getInviterName());

        if (inviter.isConnected()){
            inviter.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.IGNORED_INVITE).replace("%player%", sender.getName()));
        }

        sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.IGNORED_SUCCESSFULLY));
        invitingManager.getInvitedMap().get(sender.getName()).remove(invitation);

    }
}
