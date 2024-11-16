package dev.m7wq.qguilds.enums;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import dev.m7wq.qguilds.Plugin;
import org.jetbrains.annotations.Nullable;

import javax.management.relation.Role;

public enum RoleType {
    NO_ROLE("",0),
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

    RoleType(){}

    RoleType(String displayRole, int prestige){
        this.displayRole = displayRole;
        this.prestige = prestige;
    }

    public static @Nullable RoleType getByName(String name) {

        RoleType none = NO_ROLE;

        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(none); // Return none if no match is found
    }

    public int getPrestige() {
        return prestige;
    }
}
