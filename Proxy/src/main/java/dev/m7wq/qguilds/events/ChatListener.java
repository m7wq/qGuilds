package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.PluginProxied;
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
    
        e.setCancelled(true);



        String message = PluginProxied.getInstance().colorize(e.getMessage());
        Guild playerGuild = PluginProxied.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        ChatUtil.sendGuildMessage(player,message,playerGuild);
    }
}
