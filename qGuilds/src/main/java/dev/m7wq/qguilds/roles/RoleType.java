package dev.m7wq.qguilds.roles;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.Plugin;

public enum RoleType {
    MEMBER("&9MEMBER",1),
    SPECIAL("&bSPECIAL",2),
    MODERATOR("&2MOD",3),
    ADMIN("&cADMIN",4),
    MASTER("&eMASTER",5);

    private String displayRole;
    private int prestige;

    public String getDisplayRole() {
        return Plugin.getInstance().colorize(displayRole);
    }

    RoleType(String displayRole, int prestige){
        this.displayRole = displayRole;
        this.prestige = prestige;
    }

   public static @NotNull RoleType getByName(String string){
        return Arrays.stream(RoleType.values()).filter(roleType -> roleType.name().equalsIgnoreCase(string)).findFirst().get();
   }

    public int getPrestige() {
        return prestige;
    }
}
