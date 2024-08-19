package dev.m7wq.qguilds.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.managers.InvitingManager;
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
                    "invite",
                    "disband",
                    "accept",
                    "ignore",
                    "leave",
                    "chat",
                    "toggle",
                    "promote",
                    "demote",
                    "setrole",
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

            List<String> completer = new ArrayList<>();
            String currentArg = args[args.length-1].toLowerCase();
            for (String string : list){
                if (string.startsWith(currentArg))
                    completer.add(string);
            }

            return completer;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toggle")) {

                List<String> list = Arrays.asList("on", "off");

                List<String> completer = new ArrayList<>();
                String currentArg = args[args.length-1].toLowerCase();
                for (String string : list){
                    if (string.startsWith(currentArg))
                        completer.add(string);
                }

                return completer;

            }
        }

        if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("ignore")){
            InvitingManager manager = Plugin.getInstance().getInvitingManager();
            List<String> list = manager.getInvitationsList(sender.getName());

            List<String> completer = new ArrayList<>();
            String currentArg = args[args.length-1].toLowerCase();
            for (String string : list){
                if (string.startsWith(currentArg))
                    completer.add(string);
            }

            return completer;


        }

        List<String> playersList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()){
            playersList.add(player.getName());
        }

        List<String> completer = new ArrayList<>();
        String currentArg = args[args.length-1].toLowerCase();
        for (String string : playersList){
            if (string.startsWith(currentArg))
                completer.add(string);
        }

        return completer;


    }



}
