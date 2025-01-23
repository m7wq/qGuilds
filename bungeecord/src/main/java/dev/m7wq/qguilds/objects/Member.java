package dev.m7wq.qguilds.objects;

import dev.m7wq.qguilds.roles.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@AllArgsConstructor@Setter
public class Member {

    private Role role;
    private String ign;
    private boolean muted;


}
