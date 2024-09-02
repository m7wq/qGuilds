package dev.m7wq.qguilds.proxy.managers;

import dev.m7wq.qguilds.proxy.objects.Guild;
import dev.m7wq.qguilds.proxy.objects.Member;
import dev.m7wq.qguilds.proxy.enums.RoleType;

import java.util.Arrays;

public class MembersManager {
    private GuildsManager manager;

    public MembersManager(GuildsManager manager){
        this.manager = manager;
    }

    public String promotePlayer(String ign){
        int rolePrestige = RoleType.getByName( manager.getPlayerRole(ign)).getPrestige();

        RoleType role = Arrays.stream(RoleType.values()).filter(roleType -> (rolePrestige-roleType.getPrestige()) == -1).findFirst().get();
        return role.name();
    }

    public String demotePlayer(String ign){
        int rolePrestige = RoleType.getByName( manager.getPlayerRole(ign)).getPrestige();

        RoleType role = Arrays.stream(RoleType.values()).filter(roleType -> (rolePrestige-roleType.getPrestige()) == 1).findFirst().get();

        if(role != null){
            return role.name();
        }

        return "";
    }



    public void setRole(String ign, RoleType role){
        for (Guild guild : manager.getGuildMap().values())
        {


           for (int i = 0; i < guild.getPlayersList().size(); i++){
               if (guild.getPlayersList().get(i).getIgn().equalsIgnoreCase(ign)){
                   manager.getGuildMap().get(guild.getGuildName()).getPlayersList().get(i).setRole(role.toString());
                   return;
               }
           }


        }


    }

    public void removePlayer(String ign){
        for(Guild guild : manager.getGuildMap().values()){
            for(Member member : guild.getPlayersList()){
                if(member.getIgn().equalsIgnoreCase(ign)){
                    manager.getGuildMap().get(guild.getGuildName()).getPlayersList().remove(member);
                    break;
                }

            }
        }
    }
    public void addPlayer(Member member,String guildName){
        for (Guild guild : manager.getGuildMap().values()){
            if (guild.getGuildName().equalsIgnoreCase(guildName))
                manager.getGuildMap().get(guild.getGuildName()).getPlayersList().add(member);
        }
    }

}
