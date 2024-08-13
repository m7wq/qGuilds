package dev.m7wq.qguilds.managers;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager.Configure;

public class ConfigurationManager {


    public enum Message{
        FAILED_CREATING_GUILD("failed-creating-guild"),
        LONG_TAG("long-tag"),
        ALREADY_ON_GUILD("already-in-guild"),
        SUCCESSFULLY_CREATED("successfully-created"),
        YES("yes"),
        NO("no"),
        CONFIRM("confirm"),
        FAILED_DISBANDING("failed-disbanding"),
        NOT_ON_GUILD_TO_DISBAND("not-in-guild-to-disband"),
        NO_PERMISSION("no-permission"),
        TIME_OVER("time-out"),
        SUCCESS_DISBAND("successfully-disbanded"),
        ARENT_ON_GUILD_TO_LEAVE("not-in-guild-to-leave"),
        YOU_ARE_MASTER("you-are-the-master"),
        YOU_LEFT_GUILD("you-left-the-guild");

        String key;

        String getKey(){return this.key;}

        Message(String key){
            this.key = key;
        }
    }

    public enum Configure{
        DEFAULT_TAG_COLOR("tag.default-tag-color");
        


        String key;

        String getKey(){return this.key;}

        Configure(String key){
            this.key = key;
        }
    } 

    private FileConfiguration messagesConfig;
    private File messagesFile;
    private FileConfiguration config;

    public void loadConfigurations(Plugin instance){
        instance.getConfig().options().copyDefaults();
        instance.saveDefaultConfig();

        this.config = instance.getConfig();

        messagesFile = new File(instance.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            instance.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    FileConfiguration getMessagesConfig(){
        return this.messagesConfig;
    }

    public FileConfiguration getConfig() {
        return config;
    }


    public String getMessage(Message message){
        return Plugin.getInstance().colorize(getMessagesConfig().getString(message.getKey())).replace("%prefix%",Plugin.getInstance().getPrefix());
    }

    public List<String> getMessages(Message message){
        return getMessagesConfig().getStringList(message.getKey());
    }

    public String getConfigure(Configure configure){
        return getConfig().getString(configure.getKey());
    }


    public List<String> getConfigures(Configure configure){
        return getConfig().getStringList(configure.getKey());
    }
}
