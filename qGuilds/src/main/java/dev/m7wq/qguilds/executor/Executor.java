package dev.m7wq.qguilds.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.m7wq.qguilds.entity.Commandable;
import dev.m7wq.qguilds.subcmds.CreateSubCommand;
import dev.m7wq.qguilds.subcmds.DisbandSubCommand;
import dev.m7wq.qguilds.subcmds.LeaveSubCommand;

public class Executor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;


        Player player = (Player) sender;

        Commandable commandObject;

        switch (args[0]){
            case "create":
                commandObject = new CreateSubCommand();
                commandObject.jobLoad(player,args);
                break;
            case "disband":
                commandObject= new DisbandSubCommand();
                commandObject.jobLoad(player, args);
                break;
                case "leave":
                commandObject = new LeaveSubCommand();
                commandObject.jobLoad(player, args);
                break;
            default:

        }

        return false;
    }
}
