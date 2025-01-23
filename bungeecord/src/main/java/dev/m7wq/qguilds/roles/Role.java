package dev.m7wq.qguilds.roles;

import dev.m7wq.qguilds.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter@AllArgsConstructor
public class Role {
    String name;
    String color;
    List<Permission> permissions;
}
