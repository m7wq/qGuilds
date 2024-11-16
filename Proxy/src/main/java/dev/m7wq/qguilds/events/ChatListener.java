package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.utils.ChatUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(ChatEvent e){

        if (!(e.getSender() instanceof ProxiedPlayer))return;

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        if (!ChatUtil.chatToggleSet.contains(player.getName()))
            return;

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();


        if (!guildsManager.onGuild(player.getName())){
            Plugin.getInstance().getConfigurationManager().getMessage(player, ConfigurationManager.Message.NO_CHAT);
            return;
        }

        if (e.getMessage().startsWith("/"))return;

        e.setCancelled(true);


        if (Plugin.getInstance().getMembersManager().isMuted(player.getName())){
            ChatUtil.sendMessage(player, ConfigurationManager.Message.CANT_CHAT_MUTED);
            return;
        }




        String message = e.getMessage();
        Guild playerGuild = Plugin.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        ChatUtil.sendGuildMessage(player,message,playerGuild);
    }
}
