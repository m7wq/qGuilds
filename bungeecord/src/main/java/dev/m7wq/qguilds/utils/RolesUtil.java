package dev.m7wq.qguilds.utils;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;
import dev.m7wq.qguilds.managers.MembersManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import jdk.internal.org.jline.reader.ParsedLine;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RolesUtil {

    public static List<String> getPermissionRoles(String ign, Permission permission){


        Guild guild = Plugin.getInstance().getGuildsManager().getPlayerGuild(ign);

        List<String> roles = new ArrayList<>();

        guild.getRoles().forEach(
                role -> {
                    if (role.getPermissions().contains(permission))
                        roles.add(role.getName());
                }
        );

        return roles;
    }

    public static List<Permission> toEnumList(String[] permissions){
        List<String> permsToList = Arrays.asList(permissions);
        List<Permission> enumList = new ArrayList<>();

        permsToList.forEach(perm -> {
            enumList.add(Permission.getByName(perm));
        });

        return enumList;
    }

    public static boolean isLowerThanTargetOrEqual(ProxiedPlayer sender, String target){
        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
       MembersManager membersManager = Plugin.getInstance().getMembersManager();



        return (getMemberRoleLength(sender.getName()) >= getMemberLowerRoleLength(target));

    }

    public static int getMemberLowerRoleLength(String ign) {



       return getMemberRoleLength(ign)-1;




    }

    public static int getMemberHigherRoleLength(String ign){
        return getMemberRoleLength(ign)+1;
    }

    public static int getMemberRoleLength(String ign){
        Guild guild = Plugin.getInstance().getGuildsManager().getPlayerGuild(ign);

        Member member = guild.getPlayersList().stream().filter(member1 -> member1.getIgn().equals(ign)).findFirst().get();

        for (int i = 0; i < guild.getRoles().size(); i++) {
            if (guild.getRoles().get(i).getName().equalsIgnoreCase(member.getRole().getName())) {


                return i;

            }

        }

        return -2;
    }

    public static int getRoleLength(String roleGuy, Role role){

        Guild guild = Plugin.getInstance().getGuildsManager().getPlayerGuild(roleGuy);

        for (int i = 0; i < guild.getRoles().size(); i++) {
            if (guild.getRoles().get(i).equals(role)) {


                return i;

            }

        }
        return -2;
    }

}
