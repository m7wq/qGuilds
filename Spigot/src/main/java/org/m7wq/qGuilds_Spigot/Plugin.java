package org.m7wq.qGuilds_Spigot;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.m7wq.qGuilds_Spigot.data.DataHandlerListener;
import org.m7wq.qGuilds_Spigot.data.DataManager;
import org.m7wq.qGuilds_Spigot.placeholderapi.PAPI;

import java.util.Map;
import java.util.logging.Level;

public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    @Getter
    @Setter
    DataManager manager;


    @Override
    public void onEnable() {

        getLogger().log(Level.INFO,
                "\n"+
                        "[24:60:60 INFO]: [QGUILDS] [--------------------- Q-GUILDS ---------------------]\n" +
                        "[24:60:60 INFO]: [QGUILDS] !- Platform: Server\n" +
                        "[24:6?:60 INFO]: [QGUILDS] !- Program: qGuilds\n" +
                        "[24:60:60 INFO]: [QGUILDS] !- Description: Free customized guilds plugin\n" +
                        "[2?:60:60 INFO]: [QGUILDS] \n" +
                        "[24:60:60 INFO]: [QGUILDS] - Developer: [m7wq]\n" +
                        "[24:60:6? INFO]: [QGUILDS] - Development Team: DevVerse Minecraft\n" +
                        "[24:6?:60 INFO]: [QGUILDS] - <3 Good luck using my plugin <3\n" +
                        "[2?:60:60 INFO]: [QGUILDS] [--------------------- Q-GUILDS ---------------------]");

        DataManager dataManager = new DataManager();
        this.manager = dataManager;
        instance = this;

        DataHandlerListener dataHandlerListener = new DataHandlerListener(dataManager);


        getServer().getPluginManager().registerEvents(dataHandlerListener, this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "qGuilds");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "qGuilds");
        getServer().getMessenger().registerIncomingPluginChannel(this, "qGuilds", dataHandlerListener);




        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI().register();
        }


    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

}
