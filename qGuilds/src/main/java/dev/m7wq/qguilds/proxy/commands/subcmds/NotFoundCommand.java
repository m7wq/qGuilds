package dev.m7wq.qguilds.proxy.commands.subcmds;

import dev.m7wq.qguilds.proxy.interfaces.SubCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class NotFoundCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        return; // for now ;D
    }
}
