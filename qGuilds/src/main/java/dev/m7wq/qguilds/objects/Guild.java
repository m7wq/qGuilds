package dev.m7wq.qguilds.objects;

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
        return displayName;
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
        return guildTag;
    }

    public void setGuildTag(String guildTag) {
        this.guildTag = guildTag;
    }
}
