package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Invitation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;

public class PlayerJoinListener implements Listener {

    private InvitingManager manager;

    public PlayerJoinListener (InvitingManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        GuildsManager manager1 = Plugin.getInstance().getGuildsManager();

        Guild guild = manager1.getPlayerGuild(player.getName());

        guild.getPlayersList().forEach(member ->
        {
            Player memberPlayer = Bukkit.getPlayerExact(member.getIgn());

            if (memberPlayer.isOnline()){
                memberPlayer.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.JOIN_MESSAGE).replace("%player%", player.getName()));
            }
        });

        if (manager.getInvitedMap().containsKey(e.getPlayer().getName()))
            return;


        manager.getInvitedMap().put(player.getName(), new HashSet<Invitation>());

    }

}
