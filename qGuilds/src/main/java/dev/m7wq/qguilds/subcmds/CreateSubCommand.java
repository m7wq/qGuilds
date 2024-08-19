package dev.m7wq.qguilds.subcmds;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.entity.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.roles.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;


public class CreateSubCommand implements SubCommand {

    Set<String> nameSet = new HashSet<>();

    // COOLDOWN

    @Override
    public void jobLoad(Player sender, String... arg) {
        if (!arg[0].equalsIgnoreCase("create"))
            throw new IllegalStateException("SubCreateGuild issue on executing");




        if (arg.length != 4){
            for (String string : Plugin.getInstance().getConfigurationManager()
                    .getMessages(sender,ConfigurationManager.Message.FAILED_CREATING_GUILD)){
                sender.sendMessage(Plugin.getInstance().colorize(
                        string.replace("%prefix%",Plugin.getInstance().getPrefix())
                ));
            }
            return;
        }

        if (arg[3].length() > 3){
            sender.sendMessage(Plugin.getInstance().colorize(
                    Plugin.getInstance().getConfigurationManager()
                            .getMessage(sender,ConfigurationManager.Message.LONG_TAG)
                            .replace("%prefix%",Plugin.getInstance().getPrefix())
            ));
            return;
        }





        String playerRole = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName());

        if (Plugin.getInstance().getGuildsManager().onGuild(sender.getName())){
            Plugin.getInstance().getConfigurationManager().getMessages(sender,ConfigurationManager.Message.ALREADY_ON_GUILD).forEach(message -> {
                if (message.contains("%confirm%")) {
                    ChatUtil.sendConfirming(playerRole,message,sender);
                }else{
                    sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%",Plugin.getInstance().getPrefix())));
                }
            });
            return;
        }








        ConfigurationManager manager = Plugin.getInstance().getConfigurationManager();

        String name = arg[1];
        String displayName = arg[2];
        String tag = arg[3].toUpperCase();

        Member master = new Member(RoleType.MASTER.name(), sender.getName(), false);

        Plugin.getInstance().getMySQL().createGuild(
                master,
                new Guild(displayName, Plugin.getInstance().getConfigurationManager()
                        .getConfigure(sender,ConfigurationManager.Configure.DEFAULT_TAG_COLOR), tag, name, Arrays.asList(master))
        );

        sender.sendMessage(Plugin.getInstance().colorize(
                Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.SUCCESSFULLY_CREATED)
                        .replace("%prefix%", Plugin.getInstance().getPrefix())
        ));



    }
}
