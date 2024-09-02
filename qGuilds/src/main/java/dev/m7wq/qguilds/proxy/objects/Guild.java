package dev.m7wq.qguilds.proxy.objects;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.managers.ConfigurationManager;

import java.util.List;


public class Guild {

    String displayName;
    String guildName;
    String guildTag;
    String tagColor;
    List<Member> playersList;

    public Guild(String displayName, String tagColor, String guildTag, String guildName, List<Member> playersList) {
        this.playersList = playersList;
        this.displayName = displayName;
        this.tagColor = tagColor;
        this.guildTag = guildTag;
        this.guildName = guildName;
    }

    public List<Member> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<Member> playersList) {
        this.playersList = playersList;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getDisplayName() {
        return Plugin.getInstance().colorize( displayName+"&r");
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getGuildTag() {
        return this.guildTag;

    }

    public String getFormatedGuildTag(){
        return Plugin.getInstance().colorize(
                Plugin.getInstance().getConfigurationManager()
                        .getConfigure(ConfigurationManager.Configure.TAG_FORMAT)
                        .replace("%tag%",this.guildTag)
                        .replace("%color%",getTagColor())+"&r"
        );
    }

    public void setGuildTag(String guildTag) {
        this.guildTag = guildTag;
    }
}
