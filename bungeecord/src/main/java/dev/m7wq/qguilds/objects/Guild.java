package dev.m7wq.qguilds.objects;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.roles.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter

public class Guild {

    List<Role> roles;
    String guildName;
    String guildTag;
    String tagColor;
    List<Member> playersList;
    String oldName;


    public Guild(List<Role> roles, String tagColor, String guildTag,String guildName ,List<Member> playersList) {
        this.tagColor = tagColor;
        this.roles = roles;
        this.playersList = playersList;
        this.guildTag = guildTag;
        this.guildName = guildName;
    }

    public String getFormatedGuildTag(){
        return Plugin.getInstance().colorize(
                Plugin.getInstance().getConfigurationManager()
                        .getConfigure(ConfigurationManager.Configure.TAG_FORMAT)
                        .replace("%tag%",this.guildTag)
                        .replace("%color%",getTagColor())+"&r"
        );
    }

    public List<String> getRolesNames(){

        List<String> array = new ArrayList<>();

        for (Role role : getRoles()) {
            array.add(role.getName());
        }

        return array;

    }

    // My roles system main idea
    public Role getLowestRole(){
        return getRoles().get(getRoles().size()-1);
    }

    public Role getHighestRole(){
        return getRoles().get(0);
    }
}
