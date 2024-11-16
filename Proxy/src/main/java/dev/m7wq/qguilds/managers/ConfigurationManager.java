
package dev.m7wq.qguilds.managers;

import java.io.File;
import java.util.List;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.utils.config.QConfig;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import net.md_5.bungee.config.Configuration;

public class ConfigurationManager {


    public enum Message{
        ON_HIGHER_RANK("he-is-on-higher-rank"),
        FAILED_CREATING_GUILD("failed-creating-guild"),
        LONG_TAG("long-tag"),
        ALREADY_ON_GUILD("already-in-guild"),
        SUCCESSFULLY_CREATED("successfully-created"),
        PLAYER_OFFLINE("player-is-offline"),
        SET_PLAYER("set-the-player"),
        YES("yes"),
        NO("no"),
        PERMISSIONS_MESSAGE("permissions-message"),
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
        CANT_DEMOTE_YOURSELF("cant-demote-yourself"),
        CANT_CHANGE_ROLE("cant-change-role"),
        CANT_PROMOTE_HIGHER_ROLE_PLAYER("you-cant-promote-higher-than-you"),
        CANT_DEMOTE("you-cant-demote"),
        ENABLED_TOGGLE("enabled-toggle"),
        ALREADY_OFF("already-off"),
        DISABLED_TOGGLE("disabled-toggle"),
        CANT_PROMOTE("you-cant-promote"),
        CANT_PROMOTE_YOURSELF("cant-promote-yourself"),
        ALREADY_LOWEST_ROLE("you-cant-demote-anymore"),
        ALREADY_INVITED("already-invited"),
        NO_CHAT("not-in-guild-to-chat"),
        NOT_IN_YOUR_GUILD("not-in-your-guild"),
        ROLE_IS_SET("role-is-set"),
        SUCCESSFULLY_TRANSFERRED("transferred-successfully"),
        YOU_ARE_NOW_OWNER("you-are-now-owner"),
        CANT_TRANSFER_TO_YOURSELF("cant-transfer-to-your-self"),
        SET_ROLE_AND_PLAYER("set-role-and-player"),
        LONG_OR_SHORT_TAG("long-or-short-tag"),
        ENTER_THE_TAG("please-set-the-tag"),
        ENTER_TAG_COLOR("please-set-the-tag_color"),
        LONG_OR_SHORT_TAG_COLOR("long-or-short-tag_color"),
        CHANGED_TAG_COLOR("changed-tag-color-successfully"),
        CHANGED_TAG("changed-tag-successfully"),
        CANT_RENAME("cant-rename"),
        COLOR_EXAMPLE("color-example"),
        SUCCESSFULLY_RENAMED("successfully-renamed"),
        CANT_SEND_LIST("cant-send-list"),
        UN_ALLOWED_TO_SEND_LIST("this-is-not-allowed"),
        LIST_MESSAGE("list-message"),
        ONLINE_PLAYER_IN_LIST("online-player"),
        OFFLINE_PLAYER_IN_LIST("offline-player"),
        BETWEEN_EVERY_PLAYER_IN_LIST("between-every-player-in-the-list"),
        WRONG_ROLE("theres-no-role"),
        NOT_IN_GUILD_TO_LIST("you-arent-in-guild-to-see-list"),
        CANT_CHAT_MUTED("cant-chat-because-muted"),
        ALREADY_MUTED("already-muted"),
        MUTED_SUCCESSFULLY("successfully-muted"),
        NOT_MUTED("not-muted"),
        CONTAIN_SPECIAL_CHAR("text-contain-special-character"),
        LONG_NAME("long-name"),
        UNMUTED_SUCCESSFULLY("successfully-unmuted"),
        CANT_MUTE_HIGHER("cant-mute-higher-than-you"),
        CANT_UNMUTE_HIGHER("cant-unmute-higher-than-you"),
        CANT_MUTE_YOURSELF("cant-mute-your-self"),
        CANT_UNMUTE_YOURSELF("you-cant-unmute-your-self"),
        KICKED_SUCCESSFULLY("kicked-successfully"),
        NOT_COLOR_CODE("not-color-code"),
        YOU_HAVE_BEEN_KICKED("you-have-been-kicked"),
        YOU_HAVE_BEEN_MUTED("you-have-been-muted"),
        YOU_HAVE_BEEN_UNMUTED("you-have-been-unmuted"),
        COOLDOWN_MESSAGE("under-cooldown"),
        NOT_EXISTS_GUILD("this-guild-is-not-exists");



        public String key;

        public String getKey(){return this.key;}

        Message(String key){
            this.key = key;
        }
    }

    public enum Configure{
        DEFAULT_TAG_COLOR("tag.default-tag-color"),
        CAN_INVITE("permissions.can-invite"),
        CAN_UNMUTE("permissions.can-unmute"),
        CAN_KICK("permissions.can-kick"),
        CAN_MUTE("permissions.can-mute"),
        TAG_FORMAT("tag.format"),
        NOT_FOUND_GUILD("guild.not-found-guild"),
        NOT_FOUND_ROLE("guild.not-found-role"),
        CAN_PROMOTE("permissions.can-promote"),
        CAN_DEMOTE("permissions.can-demote"),
        CAN_SET_TAG("permissions.can-setTag"),
        CAN_SET_TAG_COLOR("permissions.can-setTagColor"),
        CAN_RENAME("permissions.can-rename"),
        ROLE_AFTER_TRANSFER("options.role-after-transfer"),
        ALLOW_CHECKING_OTHER_GUILDS_LISTS("allow-to-see-list-of-other-guilds"),
        TAG_COOLDOWN("cooldowns.settag"), // DAYS
        RENAME_COOLDOWN("cooldowns.rename"), // DAYS
        CREATE_COOLDOWN("cooldowns.create"), // DAYS
        TAG_COLOR_COOLDOWN("cooldowns.settagcolor"); // HOUR


        String key;

        public String getKey(){return this.key;}

        Configure(String key){
            this.key = key;
        }
    }

    private QConfig messagesConfig;
    public  QConfig cooldowns;
    private QConfig config;

    public ConfigurationManager(Plugin instance){
        File path = instance.getDataFolder();

        messagesConfig = new QConfig(path,"messages.yml");
        config = new QConfig(path,"settings.yml");
        cooldowns = new QConfig(path,"cooldowns.yml");
    }


    public Configuration getMessagesConfig(){
        return this.messagesConfig.getConfig();
    }
    public Configuration getCooldowns(){return this.cooldowns.getConfig();}
    public Configuration getConfig() {
        return config.getConfig();
    }


    public String getMessage(ProxiedPlayer player, Message message){
        if (!getMessagesConfig().contains(message.getKey()))
            throw new IllegalStateException("No config key found on the configuration");


        String string = Plugin.getInstance().colorize(getMessagesConfig().getString(message.getKey())).replace("%prefix%", Plugin.getInstance().getPrefix());
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
