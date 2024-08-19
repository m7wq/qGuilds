package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){
        if (!ChatUtil.chatToggleSet.contains(e.getPlayer().getName()))
            return;
    
        e.setCancelled(true);

        Player player = e.getPlayer();

        String message = Plugin.getInstance().colorize(e.getMessage());
        Guild playerGuild = Plugin.getInstance().getGuildsManager().getPlayerGuild(player.getName());

        ChatUtil.sendGuildMessage(player,message,playerGuild);
    }
}
