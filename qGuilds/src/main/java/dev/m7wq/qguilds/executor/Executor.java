package dev.m7wq.qguilds.executor;


import dev.m7wq.qguilds.subcmds.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.m7wq.qguilds.entity.SubCommand;

public class Executor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;


        Player player = (Player) sender;

        SubCommand commandObject = null;

        switch (args[0]){
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

        if (commandObject != null){
            commandObject.jobLoad(player,args);
        }

        return false;
    }
}
