package dev.m7wq.qguilds.proxy.managers;

import java.util.ArrayList;
import java.util.HashMap;


import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.managers.ConfigurationManager.Configure;
import dev.m7wq.qguilds.proxy.mysql.MySQL;
import dev.m7wq.qguilds.proxy.objects.Guild;
import dev.m7wq.qguilds.proxy.objects.Member;



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


        Plugin.getInstance().getProxy().getScheduler().runAsync(Plugin.getInstance(), () -> {



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
       return new Guild(Plugin.getInstance().getConfigurationManager().getConfigure(Configure.NOT_FOUND_GUILD),
        "", "", Plugin.getInstance().getConfigurationManager().getConfigure(Configure.NOT_FOUND_GUILD), new ArrayList<>());
    }

    public @NotNull String getPlayerRole(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign))
                    return member.getRole();

            }
        }
        return Plugin.getInstance().getConfigurationManager().getConfigure(Configure.NOT_FOUND_ROLE);

    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
