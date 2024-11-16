package dev.m7wq.qguilds.commands;


import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.commands.subcmds.*;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.managers.InvitingManager;


import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
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

        if (args.length >= 2 && args[0].equalsIgnoreCase("mypermissions")){
            return new ArrayList<>();
        }

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
                    "transfer",
                    "unmute",
                    "muteall",
                    "unmuteall",
                    "kick",
                    "mypermissions",
                    "help"
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

            if (args[0].equalsIgnoreCase("invite")){
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

            if (Arrays.asList("promote","demote","setrole","mute","unmute","kick").contains(args[0])){

                GuildsManager guildsManager  = Plugin.getInstance().getGuildsManager();

                List<String> list = new ArrayList<>();

                for (Member member : guildsManager.getPlayerGuild(sender.getName()).getPlayersList()){
                    list.add(member.getIgn());
                }

                List<String> completer = new ArrayList<>();
                String currentArg = args[args.length-1].toLowerCase();
                for (String string : list){
                    if (string.startsWith(currentArg))
                        completer.add(string);
                }

                return completer;


            }

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

            ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();
            boolean isAllowed = cfg.getConfig().getBoolean(ConfigurationManager.Configure.ALLOW_CHECKING_OTHER_GUILDS_LISTS.getKey());

            GuildsManager mngr = Plugin.getInstance().getGuildsManager();

            if (args[0].equalsIgnoreCase("list") && isAllowed){

                List<String> list = new ArrayList<>();

                for (Guild guild : mngr.getGuildMap().values()){
                    list.add(guild.getGuildName());
                }

                List<String> completer = new ArrayList<>();

                String currentArg = args[args.length-1].toLowerCase();

                for (String string : list){
                    String stringInLowerCase = string.toLowerCase();
                    if (stringInLowerCase.startsWith(currentArg))
                        completer.add(string);
                }

                return completer;
            }

        }

        if (args.length==3){
            if (args[0].equalsIgnoreCase("setrole")){
                List<String> list = Arrays.asList("MEMBER","MODERATOR","SPECIAL","ADMIN");

                List<String> completer = new ArrayList<>();
                String currentArg = args[args.length-1].toUpperCase();
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

        return new ArrayList<>();
    }

    public Executor() {
        super("guild","","guilds","g","clans","clan");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer))
            return;





        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (strings.length == 0){
            new HelpSubCommand().jobLoad(player,strings);
            return;
        }

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
            case "setrole":
                commandObject = new SetRoleSubCommand();
                break;
            case "transfer":
                commandObject = new TransferSubCommand();
                break;
            case "settag":
                commandObject = new SetTagSubCommand();
                break;
            case "settagcolor":
                commandObject = new SetTagColorSubCommand();
                break;
            case "rename":
                commandObject = new RenameSubCommand();
                break;
            case "list":
                commandObject = new ListSubCommand();
                break;
            case "mute":
                commandObject = new MuteSubCommand();
                break;
            case "unmute":
                commandObject = new UnMuteSubCommand();
                break;
            case "kick":
                commandObject = new KickSubCommand();
                break;
            case "mypermissions":
                commandObject = new MyPermissionsSubCommand();
                break;
            case "help":
                commandObject = new HelpSubCommand();
                break;
            default:
                commandObject = new NotFoundCommand();

        }


            commandObject.jobLoad(player,strings);

    }
}
