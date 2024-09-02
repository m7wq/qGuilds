package dev.m7wq.qguilds.proxy.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.md_5.bungee.api.connection.ProxiedPlayer;


import dev.m7wq.qguilds.proxy.objects.Invitation;

public class InvitingManager {

    private final HashMap<String, Set<Invitation>> invitedMap = new HashMap<>();

    public HashMap<String, Set<Invitation>> getInvitedMap() {
        return invitedMap;
    }

    public boolean isInvitedByTheClan(ProxiedPlayer target, String senderGuild){

        for (Invitation invitation : getInvitedMap().get(target.getName())) {
            if (invitation.getGuild().getGuildName().equalsIgnoreCase(senderGuild)){
                return true;

            }
        }



        return false;
    }

    public List<String> getInvitationsList(String sender){

        List<String> list = new ArrayList<>();

        getInvitedMap().get(sender).forEach(invitation -> list.add(invitation.getGuild().getGuildName()));

        return list;
    }

}
