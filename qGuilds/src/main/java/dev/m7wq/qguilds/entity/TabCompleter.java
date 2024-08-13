package dev.m7wq.qguilds.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCompleter implements org.bukkit.command.TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = Arrays.asList(
                    "create",
                    "disband",
                    "accept",
                    "ignore",
                    "leave",
                    "chat",
                    "toggle",
                    "prmote",
                    "demote",
                    "setrank",
                    "settag",
                    "settagcolor",
                    "rename",
                    "list",
                    "mute",
                    "unmute",
                    "muteall",
                    "unmuteall",
                    "kick",
                    "mypermissions"
            );

            return list;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toggle")) {

                return Arrays.asList("on", "off");
            }
        }

        List<String> playersList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()){
            playersList.add(player.getName());
        }

        return playersList;

    }



}
