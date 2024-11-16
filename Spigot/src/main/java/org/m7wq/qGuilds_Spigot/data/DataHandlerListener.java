package org.m7wq.qGuilds_Spigot.data;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.m7wq.qGuilds_Spigot.Plugin;

public class DataHandlerListener implements Listener, PluginMessageListener {

    private final DataManager manager;

    public DataHandlerListener(DataManager dataManager) {
        this.manager = dataManager;
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("qGuilds")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (!subChannel.equalsIgnoreCase("qGuilds")) {
            return;
        }

        try {
            String playerName = in.readUTF();
            String name = in.readUTF();
            String displayName = in.readUTF();
            String tag = in.readUTF();
            String role = in.readUTF();
            String displayRole = in.readUTF();

            manager.getDataMap().put(playerName, new PlayerData(name, displayName, tag, role, displayRole));


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
