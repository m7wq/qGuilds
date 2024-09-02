package dev.m7wq.qguilds.proxy.events;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;
import dev.m7wq.qguilds.proxy.managers.GuildsManager;
import dev.m7wq.qguilds.proxy.objects.Guild;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onQuit(ServerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        String name = player.getName();

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        Guild guild = manager.getPlayerGuild(name);

        if(guild.getPlayersList().isEmpty())
            return;

        guild.getPlayersList().forEach(member -> {
            ProxiedPlayer memberPlayer = Plugin.getInstance().getProxy().getPlayer(member.getIgn());

            if (memberPlayer.isConnected()){
                memberPlayer.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(memberPlayer, ConfigurationManager.Message.LEAVE_MESSAGE).replace("%player%",player.getName()));
            }
        });
    }
}
