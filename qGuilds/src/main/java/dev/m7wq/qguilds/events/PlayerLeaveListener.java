package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        String name = player.getName();

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        Guild guild = manager.getPlayerGuild(name);

        guild.getPlayersList().forEach(member -> {
            Player memberPlayer = Bukkit.getPlayerExact(member.getIgn());

            if (memberPlayer.isOnline()){
                memberPlayer.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.LEAVE_MESSAGE).replace("%player%",player.getName()));
            }
        });
    }
}
