package dev.m7wq.qguilds.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.mysql.MySQL;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;

public class GuildsManager {

    private final HashMap<String, Guild> guildMap = new HashMap<>();

    private final HashMap<String, Guild> invitedMap = new HashMap<>();

    public HashMap<String, Guild> getInvitedMap() {
        return invitedMap;
    }

    private MySQL mySQL;

    public HashMap<String, Guild> getGuildMap() {
        return guildMap;
    }

    public void load(MySQL mySQL){
        this.mySQL = mySQL;
        guildMap.putAll(mySQL.getGuilds());

        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> {
           mySQL.saveCacheData(getGuildMap());
        });



    }





    public void removePlayer(String ign){
        for(Guild guild : getGuildMap().values()){
            for(Member member : guild.getPlayersList()){
                if(member.getIgn().equalsIgnoreCase(ign)){
                    getGuildMap().get(guild.getGuildName()).getPlayersList().remove(member);
                    break;
                }
                
            }
        }
    }

    public boolean onGuild(String ign){
        for (Guild guild : getGuildMap().values()){

                for (Member member : guild.getPlayersList()){
                    if (member.getIgn().equalsIgnoreCase(ign))
                        return true;
                }

        }

        return false;
    }

    public Guild getPlayerGuild(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList())
                if (member.getIgn().equalsIgnoreCase(ign))
                    return guild;
        }
        throw new IllegalStateException("No guild found for the player's name! getPlayerGuild#Method");
    }

    public String getPlayerRole(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign))
                    return member.getRole();

            }
        }
        throw new IllegalStateException("No role found for the player's name! getPlayerRole#Method");

    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
