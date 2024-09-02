package dev.m7wq.qguilds.proxy.events;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;
import dev.m7wq.qguilds.proxy.managers.GuildsManager;
import dev.m7wq.qguilds.proxy.managers.InvitingManager;
import dev.m7wq.qguilds.proxy.objects.Guild;
import dev.m7wq.qguilds.proxy.objects.Invitation;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


import java.util.HashSet;

public class PlayerJoinListener implements Listener {

    private InvitingManager manager;

    public PlayerJoinListener (InvitingManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void playerJoin(ServerConnectEvent e){

        ProxiedPlayer player = e.getPlayer();

        if (!manager.getInvitedMap().containsKey(e.getPlayer().getName())) {


            manager.getInvitedMap().put(player.getName(), new HashSet<Invitation>());
        }

        GuildsManager manager1 = Plugin.getInstance().getGuildsManager();

        if (!manager1.onGuild(player.getName()))
            return;

        Guild guild = manager1.getPlayerGuild(player.getName());

        if(guild.getPlayersList().isEmpty())
            return;

        guild.getPlayersList().forEach(member ->
        {
            ProxiedPlayer memberPlayer = Plugin.getInstance().getProxy().getPlayer(member.getIgn());

            if (memberPlayer.isConnected()){
                memberPlayer.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.JOIN_MESSAGE).replace("%player%", player.getName()));
            }
        });



    }

}
