package dev.m7wq.qguilds.entity;

import org.bukkit.entity.Player;

public interface Commandable {

    void jobLoad(Player sender, String...arg);
}