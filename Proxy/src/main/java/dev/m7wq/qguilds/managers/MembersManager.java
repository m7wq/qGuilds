package dev.m7wq.qguilds.managers;

import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class MembersManager {
    private GuildsManager manager;

    public MembersManager(GuildsManager manager){
        this.manager = manager;
    }

    public boolean isMuted(String ign){
        return getMember(ign).isMuted();
    }

    public void setMuted(String ign, boolean bool){
        getMember(ign).setMuted(bool);
    }

    public void kick(String ign){
        for (Guild guild : manager.getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign)){
                    guild.getPlayersList().remove(member);
                    return;
                }
            }
        }
    }

    public Member getMember(String ign){
        for (Guild guild : manager.getGuildMap().values()){
            for (Member member : guild.getPlayersList()){
                if (member.getIgn().equalsIgnoreCase(ign))
                    return member;
            }
        }
        return null;
    }

    public String promotePlayer(String ign) {

        RoleType crRole = RoleType.getByName(manager.getPlayerRole(ign));

        RoleType role = null;


        switch (crRole) {
            case MEMBER:
                role = RoleType.SPECIAL;
                break;
            case SPECIAL:
                role = RoleType.MODERATOR;
                break;
            case MODERATOR:
                role = RoleType.ADMIN;
                break;
            case ADMIN:
                role = RoleType.MASTER;
                break;
            default:

                return null;
        }


        if (role == null) {
            return null;
        }


        return role.name();
    }


    public @Nullable String demotePlayer(String ign){
        RoleType crRole = RoleType.getByName( manager.getPlayerRole(ign));

        RoleType role = null;

        if (crRole==RoleType.ADMIN){
            role = RoleType.MODERATOR;
        }else if (crRole==RoleType.MODERATOR){
            role = RoleType.SPECIAL;
        } else if (crRole==RoleType.SPECIAL) {
            role = RoleType.MEMBER;
        }


        if (role==null)
            return null;
        return role.name();
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
