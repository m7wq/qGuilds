package dev.m7wq.qguilds.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.roles.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatSubCommand implements SubCommand {
    @Override
    public void jobLoad(Player sender, String... arg) {
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        String playerName = sender.getName();
        Guild playerGuild = guildsManager.getPlayerGuild(playerName);
        String guildDisplayName = playerGuild.getDisplayName();
        String playerRole = RoleType.getByName(guildsManager.getPlayerRole(playerName)).getDisplayRole();

        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < arg.length; i++){
            builder.append(arg[i]).append(" ");
        }
        String message = Plugin.getInstance().colorize(builder.toString());

        ChatUtil.sendGuildMessage(sender,message,playerGuild);




    }
}
