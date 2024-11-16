package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

public class PromoteSubCommand implements SubCommand {

    private final Set<String> canPromote = new HashSet<>(
            Plugin.getInstance().getConfigurationManager().getConfigures(ConfigurationManager.Configure.CAN_PROMOTE)
    );

    @Override
    public void jobLoad(ProxiedPlayer sender, String... args) {
        ConfigurationManager messages = Plugin.getInstance().getConfigurationManager();

        String role = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName());


        if (!canPromote.contains(role)) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.NO_PERMISSION));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.SET_PLAYER));
            return;
        }



        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();



        String target = args[1];

        if(!guildsManager.onGuild(target,guildsManager.getPlayerGuild(sender.getName()).getGuildName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_YOUR_GUILD);
            return;
        }




        if (target.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE_YOURSELF));
            return;
        }

        MembersManager manager = Plugin.getInstance().getMembersManager();

        RoleType senderRole = RoleType.getByName(role);
        RoleType targetRole = RoleType.getByName(Plugin.getInstance().getGuildsManager().getPlayerRole(target));



        if (senderRole == null || targetRole == null) {
            throw new IllegalStateException("No Role found");
        }


        if (targetRole.getPrestige() >= senderRole.getPrestige()) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE_HIGHER_ROLE_PLAYER));
            return;
        }

        String promotedRole = manager.promotePlayer(target);


        RoleType newRole = RoleType.getByName(promotedRole);
        if (newRole == null) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.ON_HIGHER_RANK));
            return;
        }


        if (newRole == RoleType.MASTER) {
            for (String message : Plugin.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.ON_HIGHER_RANK)) {
                sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%", Plugin.getInstance().getPrefix())));
            }
            return;
        }


        if (newRole.getPrestige() >= senderRole.getPrestige()) {
            sender.sendMessage(messages.getMessage(sender, ConfigurationManager.Message.CANT_PROMOTE));
            return;
        }


        manager.setRole(target, newRole);
        sender.sendMessage(Plugin.getInstance().colorize(
                messages.getMessage(sender, ConfigurationManager.Message.PLAYER_PROMOTED)
                        .replace("%rank%", newRole.getDisplayRole())
                        .replace("%player%", target)
        ));

        BungeeHook.callEvent(new ChangeHappenEvent(null,target));
    }
}
