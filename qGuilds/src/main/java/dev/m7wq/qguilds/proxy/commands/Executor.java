package dev.m7wq.qguilds.proxy.commands;


import dev.m7wq.qguilds.proxy.Plugin;
import dev.m7wq.qguilds.proxy.commands.subcmds.*;
import dev.m7wq.qguilds.proxy.managers.InvitingManager;
import dev.m7wq.qguilds.proxy.subcmds.*;


import dev.m7wq.qguilds.proxy.interfaces.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Executor extends Command implements TabExecutor {

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer))
            return null;

        ProxiedPlayer sender = (ProxiedPlayer) commandSender;

        String[] args = strings;

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

        for (ProxiedPlayer player : sender.getServer().getInfo().getPlayers()){
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

    public Executor() {
        super("guild","","guilds","g","clans","clan");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer))
            return;


        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        SubCommand commandObject = null;

        switch (strings[0]){
            case "create":
                commandObject = new CreateSubCommand();
                break;
            case "disband":
                commandObject= new DisbandSubCommand();
                break;
            case "leave":
                commandObject = new LeaveSubCommand();
                break;
            case "invite":
                commandObject = new InviteSubCommand();
                break;
            case "accept":
                commandObject = new AcceptSubCommand();

                break;
            case "ignore":
                commandObject = new IgnoreSubCommand();
                break;
            case "chat":
                commandObject = new ChatSubCommand();
                break;
            case "toggle":
                commandObject = new ToggleSubCommand();
                break;
            case "promote":
                commandObject = new PromoteSubCommand();
                break;
            case "demote":
                commandObject = new DemoteSubCommand();
                break;
            default:
                commandObject = new NotFoundCommand();

        }


            commandObject.jobLoad(player,strings);

    }
}
