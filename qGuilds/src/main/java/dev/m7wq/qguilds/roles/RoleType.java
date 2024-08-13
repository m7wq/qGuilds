package dev.m7wq.qguilds.roles;

import dev.m7wq.qguilds.Plugin;

public enum RoleType {
    MEMBER("&9MEMBER"),
    SPECIAL("&bSPECIAL"),
    MODERATOR("&2MOD"),
    ADMIN("&cADMIN"),
    MASTER("&eMASTER");

    private String displayRole;

    public String getDisplayRole() {
        return Plugin.getInstance().colorize(displayRole);
    }

    RoleType(String displayRole){
        this.displayRole = displayRole;
    }

}
