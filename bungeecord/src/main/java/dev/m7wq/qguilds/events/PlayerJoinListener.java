package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Invitation;
import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
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
    public void playerJoin(PostLoginEvent e){

        ProxiedPlayer player = e.getPlayer();

        BungeeHook.sendData(e.getPlayer().getName());


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

            if (memberPlayer != null){
                if (memberPlayer.isConnected()) {
                    memberPlayer.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.JOIN_MESSAGE).replace("%player%", player.getName()));
                }
             }
        });



    }

}
