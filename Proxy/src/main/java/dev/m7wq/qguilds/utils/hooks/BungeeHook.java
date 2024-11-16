package dev.m7wq.qguilds.utils.hooks;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import io.netty.util.internal.ObjectUtil;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class BungeeHook {


    public static boolean isConnected(String ign){
        ProxiedPlayer player = Plugin.getInstance().getProxy().getPlayer(ign);
        if (player==null){
            return false;
        }
        return player.isConnected();
    }

    public static void callEvent(Event event){
        Plugin.getInstance().getProxy().getPluginManager().callEvent(event);
    }

    public static void sendData(String player){
        GuildsManager manager = Plugin.getInstance().getGuildsManager();

        String role = manager.getPlayerRole(player);
        String displayRole = RoleType.getByName(role).getDisplayRole();
        Guild guild = manager.getPlayerGuild(player);
        String tag = guild.getFormatedGuildTag();
        String name = guild.getGuildName();
        String displayName = guild.getDisplayName();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("qGuilds");
        out.writeUTF(player);
        out.writeUTF(name);
        out.writeUTF(displayName);
        out.writeUTF(tag);
        out.writeUTF(role);
        out.writeUTF(displayRole);

        for (ServerInfo serverInfo : Plugin.getInstance().getProxy().getServers().values()){
            serverInfo.sendData("qGuilds",out.toByteArray());
        }

        System.out.println("SENT");
    }

    public static void sendData(ProxiedPlayer player){

        GuildsManager manager = Plugin.getInstance().getGuildsManager();

        Guild guild = manager.getPlayerGuild(player.getName());

        for (Member member : guild.getPlayersList()){
            sendData(member.getIgn());
        }

    }


}
