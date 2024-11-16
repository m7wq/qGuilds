package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class ChatSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        GuildsManager guildsManager = PluginProxied.getInstance().getGuildsManager();

        String playerName = sender.getName();
        Guild playerGuild = guildsManager.getPlayerGuild(playerName);
        String guildDisplayName = playerGuild.getDisplayName();
        String playerRole = RoleType.getByName(guildsManager.getPlayerRole(playerName)).getDisplayRole();

        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < arg.length; i++){
            builder.append(arg[i]).append(" ");
        }
        String message = PluginProxied.getInstance().colorize(builder.toString());

        ChatUtil.sendGuildMessage(sender,message,playerGuild);




    }
}
