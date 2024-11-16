
package dev.m7wq.qguilds.managers;

import java.util.List;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.utils.config.SimpleConfig;

import net.md_5.bungee.api.connection.ProxiedPlayer;


import net.md_5.bungee.config.Configuration;

public class ConfigurationManager {


    public enum Message{
        FAILED_CREATING_GUILD("failed-creating-guild"),
        LONG_TAG("long-tag"),
        ALREADY_ON_GUILD("already-in-guild"),
        SUCCESSFULLY_CREATED("successfully-created"),
        PLAYER_OFFLINE("player-is-offline"),
        SET_PLAYER("set-the-player"),
        YES("yes"),
        NO("no"),
        CONFIRM("confirm"),
        FAILED_DISBANDING("failed-disbanding"),
        NOT_ON_GUILD_TO_DISBAND("not-in-guild-to-disband"),
        ALREADY_IN_YOUR_GUILD("already-in-your-guild"),
        JOIN_MESSAGE("join-message"),
        LEAVE_MESSAGE("leave-message"),
        NO_PERMISSION("no-permission"),
        SUCCESS_DISBAND("successfully-disbanded"),
        FAILED_INVITING("failed-inviting"),
        PLAYER_DEMOTED("player-demoted"),
        ARENT_ON_GUILD_TO_LEAVE("not-in-guild-to-leave"),
        YOU_ARE_MASTER("you-are-the-master"),
        INVITE_MESSAGE("invite-message"),
        YOU_LEFT_GUILD("you-left-the-guild"),
        CANT_INVITE_YOURSELF("cant-invite-yourself"),
        INVITED_SUCCESSFULLY("invited-successfully"),
        EXPIRED_INVITE("expired-invite"),
        NO_INVITES_RECEIVED("no-invites-received"),
        FAILED_ACCEPTING("failed-accepting"),
        SUCCESSFULLY_ACCEPTED("accepted-successfully"),
        FAILED_IGNORING("failed-ignoring"),
        IGNORED_INVITE("ignored-invite"),
        IGNORED_SUCCESSFULLY("ignored-successfully"),
        GUILD_MESSAGE_FORMAT("guild-message-format"),
        NOT_VALID_DATA("enter-valid-data"),
        JOIN_GUILD_MESSAGE("join-guild-message"),
        ALREADY_ON("already-on"),
        PLAYER_PROMOTED("player-promoted"),
        CANT_PROMOTE_HIGHER_ROLE_PLAYER("you-cant-promote-higher-than-you"),
        CANT_DEMOTE("cant-demote"),
        ENABLED_TOGGLE("enabled-toggle"),
        ALREADY_OFF("already-off"),
        DISABLED_TOGGLE("disabled-toggle"),
        CANT_PROMOTE("you-cant-promote"),
        CANT_PROMOTE_YOURSELF("cant-promote-yourself"),
        ALREADY_LOWEST_ROLE("you-cant-demote-anymore"),
        ALREADY_INVITED("already-invited");

        String key;

        String getKey(){return this.key;}

        Message(String key){
            this.key = key;
        }
    }

    public enum Configure{
        DEFAULT_TAG_COLOR("tag.default-tag-color"),
        CAN_INVITE("permissions.can-invite"),
        CAN_KICK("permissions.can-kick"),
        CAN_MUTE("permissions.can-mute"),
        TAG_FORMAT("tag.format"),
        NOT_FOUND_GUILD("guild.not-found-guild"),
        NOT_FOUND_ROLE("guild.not-found-role"),
        CAN_PROMOTE("permissions.can-promote"),
        CAN_DEMOTE("permissions.can-demote");

        String key;

        String getKey(){return this.key;}

        Configure(String key){
            this.key = key;
        }
    }

    private SimpleConfig messagesConfig;

    private SimpleConfig config;


    public void loadConfigurations(PluginProxied instance){


        String path = instance.getDataFolder().getPath();

        messagesConfig = new SimpleConfig("messages.yml",path);
        config = new SimpleConfig("config.yml",path);
    }

    public Configuration getMessagesConfig(){
        return this.messagesConfig.getConfig();
    }

    public Configuration getConfig() {
        return config.getConfig();
    }


    public String getMessage(ProxiedPlayer player, Message message){
        if (!getMessagesConfig().contains(message.getKey()))
            throw new IllegalStateException("No config key found on the configuration");


        String string = PluginProxied.getInstance().colorize(getMessagesConfig().getString(message.getKey())).replace("%prefix%", PluginProxied.getInstance().getPrefix());
        return string;
    }

    public List<String> getMessages(ProxiedPlayer player, Message message){

        if (!getMessagesConfig().contains(message.getKey()))
            throw new IllegalStateException("No config key found on the configuration");





        return getMessagesConfig().getStringList(message.getKey());
    }

    public String getConfigure(ProxiedPlayer player,Configure configure){

        if (!getConfig().contains(configure.getKey()))
            throw new IllegalStateException("No config key found on the configuration");



        String string = getConfig().getString(configure.getKey());
        return string;
    }

    public String getConfigure(Configure configure){
        if (!getConfig().contains(configure.getKey()))
            throw new IllegalStateException("No config key found on the configuration");
        String string = getConfig().getString(configure.getKey());
        return string;
    }


    public List<String> getConfigures(Configure configure){
        if (!getConfig().contains(configure.getKey()))
            throw new IllegalStateException("No config key found on the configuration");
        return getConfig().getStringList(configure.getKey());
    }
}
