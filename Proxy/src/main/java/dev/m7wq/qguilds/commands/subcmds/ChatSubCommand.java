package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class ChatSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        String playerName = sender.getName();

        if (!guildsManager.onGuild(playerName)){
            Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.NO_CHAT);
            return;
        }

        if (Plugin.getInstance().getMembersManager().isMuted(sender.getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_CHAT_MUTED);
            return;
        }




        Guild playerGuild = guildsManager.getPlayerGuild(playerName);


        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < arg.length; i++){
            builder.append(arg[i]).append(" ");
        }
        String message = builder.toString();

        ChatUtil.sendGuildMessage(sender,message,playerGuild);




    }
}
