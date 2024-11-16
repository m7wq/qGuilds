package dev.m7wq.qguilds.events.custom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@AllArgsConstructor
public class ChangeHappenEvent extends Event {

   @Getter
   private final ProxiedPlayer master;
   @Getter
    private final String member;
}
