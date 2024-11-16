package dev.m7wq.qguilds.spigot;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable(){
        // Opening //

        getLogger().log(Level.ALL,
                "[----------------------------]/n" +
                        "!- Platform: Spigot/n"+
                        "!- Program: qGuilds/n"+
                        "!- Description: Free customized guilds plugin/n"+
                        "/n"+
                        "- Developer: [m7wq]/n"+
                        "- Development: DevVerse Minecraft/n"+
                        "- Good luck while using my plugin :D/n"+
                        "[----------------------------]");
    }

    @Override
    public void onDisable(){

    }
}
