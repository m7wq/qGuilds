package dev.m7wq.qguilds.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.mysql.MySQL;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import org.jetbrains.annotations.NotNull;


public class GuildsManager{

    private final HashMap<String, Guild> guildMap = new HashMap<>();
    private GuildsManager INSTACNE = this;

    private MySQL mySQL;




    public HashMap<String, Guild> getGuildMap() {
        return guildMap;
    }

    public List<String> getGuildsNames(){
        List<String> names = new ArrayList<>();

        for (Guild guild : getGuildMap().values()){
            names.add(guild.getGuildName());
        }

        return names;
    }

    public List<String> getMastersNames(){

        List<String> names = new ArrayList<>();

        for (Guild guild: getGuildMap().values()){
            names.add(guild.getPlayersList().get(0).getIgn());
        }

        return names;
    }

    public void load(MySQL mySQL){


        this.mySQL = mySQL;
        guildMap.putAll(mySQL.getGuilds());


        Plugin.getInstance().getProxy().getScheduler().runAsync(Plugin.getInstance(), () -> {



           mySQL.saveCacheData(getGuildMap());



        });



    }

    public void changeMapTag(String tagColor,String guildName){
        guildMap.get(guildName).setGuildTag(tagColor);
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



    public boolean onGuild(String ign,String guildName){
        for (Guild guild : getGuildMap().values()){
            if (guild.getGuildName().equalsIgnoreCase(guildName)) {
                for (Member member: guild.getPlayersList()){
                    if (member.getIgn().equalsIgnoreCase(ign))
                        return true;
                }
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
       return new Guild("",
        "", "", "", new ArrayList<>());
    }

    public @NotNull String getPlayerRole(String ign){
        for (Guild guild : getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign))
                    return member.getRole();

            }
        }
        return "";

    }

    public MySQL getMySQL() {
        return mySQL;
    }
}
