package dev.m7wq.qguilds.proxy.events;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.objects.Guild;
import dev.m7wq.qguilds.proxy.utils.ChatUtil;
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



        String message = Plugin.getInstance().colorize(e.getMessage());
        Guild playerGuild = Plugin.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        ChatUtil.sendGuildMessage(player,message,playerGuild);
    }
}
