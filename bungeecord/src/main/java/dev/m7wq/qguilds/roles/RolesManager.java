package dev.m7wq.qguilds.roles;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.ArrayList;
import java.util.List;

public class RolesManager {

    public void setRole(String target, Role role){
        Plugin.getInstance().getMembersManager().getMember(target).setRole(role);
    }

    public List<Role> getDefaultRoles(){

        //↑↓

        List<Role> roles = new ArrayList<>();

         Plugin.getInstance().getConfigurationManager()
                .getConfigures(ConfigurationManager.Configure.DEFAULT_ROLES).forEach(
                        configure -> {
                            String[] role =  configure.split("_");
                            String name = role[0];
                            String color = role[1];
                            role[2] = role[2].replace("-","_");
                            String[] permissions = role[2].split(",");

                            List<Permission> perms = RolesUtil.toEnumList(permissions);

                            roles.add(new Role(name,color,perms));
                        }
                );



        return roles;

    }


    public Role promoteMember(String ign){

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        ProxiedPlayer player = Plugin.getInstance().getProxy().getPlayer(ign);

        Guild guild = manager.getPlayerGuild(ign);

        int i = RolesUtil.getMemberLowerRoleLength(ign);

        if (i == -1){
            if (BungeeHook.isConnected(ign)){
                ChatUtil
                        .sendMessage(player, ConfigurationManager.Message.ON_HIGHEST_RANK);
            }
            return null;
        }

        Role role = guild.getRoles().get(i);

        Member member = Plugin.getInstance().getMembersManager().getMember(ign);

        member.setRole(role);



        return role;

    }


    public Role demoteMember(String ign){

        GuildsManager manager = Plugin.getInstance().getGuildsManager();
        ProxiedPlayer player = Plugin.getInstance().getProxy().getPlayer(ign);

        Guild guild = manager.getPlayerGuild(ign);

        int i = RolesUtil.getMemberHigherRoleLength(ign);


        if (i == guild.getRoles().size()){
            if (BungeeHook.isConnected(ign)){
                ChatUtil
                        .sendMessage(player, ConfigurationManager.Message.ALREADY_LOWEST_ROLE);
            }
            return null;
        }

        Role role = guild.getRoles().get(i);

        Member member = Plugin.getInstance().getMembersManager().getMember(ign);

        member.setRole(role);



        return role;
    }

    public Role getRoleByName(String roleName, String guildName){

        Guild guild = Plugin.getInstance().getGuildsManager().getGuildMap().get(guildName);

        Role finalRole = guild.getRoles().stream().filter(role ->
            role.getName().equalsIgnoreCase(roleName)
        ).findFirst().get();

        return finalRole;
    }
}
