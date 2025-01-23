package dev.m7wq.qguilds.managers;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.Role;
import jdk.internal.org.jline.reader.ParsedLine;

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

    public void setRole(String name, Role role){
        getMember(name).setRole(role);
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
