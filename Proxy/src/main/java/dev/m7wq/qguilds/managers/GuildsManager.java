package dev.m7wq.qguilds.managers;

import java.util.ArrayList;
import java.util.HashMap;


import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.mysql.MySQL;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import org.jetbrains.annotations.NotNull;


public class GuildsManager{

    private final HashMap<String, Guild> guildMap = new HashMap<>();
    private GuildsManager INSTACNE = this;

    private MySQL mySQL;




    public HashMap<String, Guild> getGuildMap() {
        return guildMap;
    }

    public void load(MySQL mySQL){


        this.mySQL = mySQL;
        guildMap.putAll(mySQL.getGuilds());


        PluginProxied.getInstance().getProxy().getScheduler().runAsync(PluginProxied.getInstance(), () -> {



           mySQL.saveCacheData(getGuildMap());



        });



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

    public @NotNull Guild getPlayerGuild(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList())
                if (member.getIgn().equalsIgnoreCase(ign))
                    return guild;
        }
       return new Guild(PluginProxied.getInstance().getConfigurationManager().getConfigure(ConfigurationManager.Configure.NOT_FOUND_GUILD),
        "", "", PluginProxied.getInstance().getConfigurationManager().getConfigure(ConfigurationManager.Configure.NOT_FOUND_GUILD), new ArrayList<>());
    }

    public @NotNull String getPlayerRole(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign))
                    return member.getRole();

            }
        }
        return PluginProxied.getInstance().getConfigurationManager().getConfigure(ConfigurationManager.Configure.NOT_FOUND_ROLE);

    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
