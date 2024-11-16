package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.objects.Invitation;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;

public class AcceptSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        InvitingManager manager = PluginProxied.getInstance().getInvitingManager();
        ConfigurationManager messages = PluginProxied.getInstance().getConfigurationManager();

        if (arg.length != 2){
            messages.getMessages(sender,ConfigurationManager.Message.FAILED_ACCEPTING)
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

        GuildsManager guildsManager = PluginProxied.getInstance().getGuildsManager();
        String senderName =  sender.getName();

        if (guildsManager.onGuild(sender.getName())){
            messages.getMessages(sender,ConfigurationManager.Message.ALREADY_ON_GUILD).forEach(message -> {
                if (message.contains("%confirm%")) {
                    ChatUtil.sendConfirming(guildsManager.getPlayerRole(senderName),message,sender);
                }else{
                    sender.sendMessage(PluginProxied.getInstance().colorize(message.replace("%prefix%", PluginProxied.getInstance().getPrefix())));
                }
            });
            return;
        }

        InvitingManager invitingManager = PluginProxied.getInstance().getInvitingManager();

        @NotNull Invitation invite = invitingManager.getInvitedMap().get(senderName).stream()
                .filter(invitation -> invitation.getGuild().getGuildName().equalsIgnoreCase(guildName))
                .findFirst().get();

        invitingManager.getInvitedMap().get(senderName).remove(invite);

        Member member = new Member(RoleType.MEMBER.name(),senderName,false);

        PluginProxied.getInstance().getMembersManager().addPlayer(member,guildName);

        sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.SUCCESSFULLY_ACCEPTED));


        ChatUtil.sendJoinGuildMessage(sender);



    }
}
