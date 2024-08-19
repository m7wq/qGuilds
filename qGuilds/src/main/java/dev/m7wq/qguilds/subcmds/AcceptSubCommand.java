package dev.m7wq.qguilds.subcmds;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.objects.Invitation;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;

public class AcceptSubCommand implements SubCommand {
    @Override
    public void jobLoad(Player sender, String... arg) {
        InvitingManager manager = Plugin.getInstance().getInvitingManager();
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        if (arg.length != 2){
            messages.getMessages(sender,ConfigurationManager.Message.FAILED_ACCEPTING)
                    .forEach(message -> sender.sendMessage(Plugin.getInstance().colorize(
                            message.replace("%prefix%",Plugin.getInstance().getPrefix())
                    )));
            return;
        }

        String guildName = arg[1];

        if (!manager.isInvitedByTheClan(sender,guildName)){
            sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.NO_INVITES_RECEIVED));
            return;
        }

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        String senderName =  sender.getName();

        if (guildsManager.onGuild(sender.getName())){
            messages.getMessages(sender,ConfigurationManager.Message.ALREADY_ON_GUILD).forEach(message -> {
                if (message.contains("%confirm%")) {
                    ChatUtil.sendConfirming(guildsManager.getPlayerRole(senderName),message,sender);
                }else{
                    sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%",Plugin.getInstance().getPrefix())));
                }
            });
            return;
        }

        InvitingManager invitingManager = Plugin.getInstance().getInvitingManager();

        @NotNull Invitation invite = invitingManager.getInvitedMap().get(senderName).stream()
                .filter(invitation -> invitation.getGuild().getGuildName().equalsIgnoreCase(guildName))
                .findFirst().get();

        invitingManager.getInvitedMap().get(senderName).remove(invite);

        Member member = new Member(RoleType.MEMBER.name(),senderName,false);

        Plugin.getInstance().getMembersManager().addPlayer(member,guildName);

        sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.SUCCESSFULLY_ACCEPTED));


        ChatUtil.sendJoinGuildMessage(sender);



    }
}
