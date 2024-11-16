package dev.m7wq.qguilds.events;

import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onQuit(ServerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        String name = player.getName();

        GuildsManager manager = PluginProxied.getInstance().getGuildsManager();
        Guild guild = manager.getPlayerGuild(name);

        if(guild.getPlayersList().isEmpty())
            return;

        guild.getPlayersList().forEach(member -> {
            ProxiedPlayer memberPlayer = PluginProxied.getInstance().getProxy().getPlayer(member.getIgn());

            if (memberPlayer.isConnected()){
                memberPlayer.sendMessage(PluginProxied.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.LEAVE_MESSAGE).replace("%player%",player.getName()));
            }
        });
    }
}
