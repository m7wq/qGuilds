package dev.m7wq.qguilds.enums;

import dev.m7wq.qguilds.exceptions.NoMatchedPermission;

public enum Permission {
    can_mute,
    can_unMute,
    can_kick,
    can_promote,
    can_demote,
    can_setTag,
    can_rename,
    can_invite,
    can_editRoles,
    can_setTagColor,
    can_setRole;

    Permission(){}

    public static Permission getByName(String string){
        for (Permission permission : values()){
            if (permission.toString().toLowerCase().equalsIgnoreCase(string.toLowerCase()))
                return permission;


        }
        throw new NoMatchedPermission();
    }


}
