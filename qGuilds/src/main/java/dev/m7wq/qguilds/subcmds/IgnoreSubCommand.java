package dev.m7wq.qguilds.subcmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.objects.Invitation;

public class IgnoreSubCommand implements SubCommand {

    @Override
    public void jobLoad(Player sender, String... arg) {
        InvitingManager manager = Plugin.getInstance().getInvitingManager();
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();



        if (arg.length != 2){
            messages.getMessages(sender,ConfigurationManager.Message.FAILED_IGNORING)
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

        InvitingManager invitingManager = Plugin.getInstance().getInvitingManager();

        @NotNull Invitation invitation =  invitingManager.getInvitedMap().get(sender.getName())
                .stream().filter(invitation1 -> invitation1.getGuild().getGuildName().equalsIgnoreCase(guildName))
                .findFirst().get();

        invitingManager.getInvitedMap().get(sender.getName()).remove(invitation);

        Player inviter = Bukkit.getPlayerExact(invitation.getInviterName());

        if (inviter.isOnline()){
            inviter.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.IGNORED_INVITE).replace("%player%", sender.getName()));
        }

        sender.sendMessage(messages.getMessage(sender,ConfigurationManager.Message.IGNORED_SUCCESSFULLY));
        invitingManager.getInvitedMap().get(sender.getName()).remove(invitation);

    }
}
