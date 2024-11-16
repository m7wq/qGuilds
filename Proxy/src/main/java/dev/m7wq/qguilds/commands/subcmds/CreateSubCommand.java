package dev.m7wq.qguilds.commands.subcmds;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.m7wq.qguilds.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import dev.m7wq.qguilds.PluginProxied;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.utils.ChatUtil;


public class CreateSubCommand implements SubCommand {

    Set<String> nameSet = new HashSet<>();

    // COOLDOWN

    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {
        if (!arg[0].equalsIgnoreCase("create"))
            throw new IllegalStateException("SubCreateGuild issue on executing");




        if (arg.length != 4){
            for (String string : PluginProxied.getInstance().getConfigurationManager()
                    .getMessages(sender, ConfigurationManager.Message.FAILED_CREATING_GUILD)){
                sender.sendMessage(PluginProxied.getInstance().colorize(
                        string.replace("%prefix%", PluginProxied.getInstance().getPrefix())
                ));
            }
            return;
        }

        if (arg[3].length() > 3){
            sender.sendMessage(PluginProxied.getInstance().colorize(
                    PluginProxied.getInstance().getConfigurationManager()
                            .getMessage(sender,ConfigurationManager.Message.LONG_TAG)
                            .replace("%prefix%", PluginProxied.getInstance().getPrefix())
            ));
            return;
        }





        String playerRole = PluginProxied.getInstance().getGuildsManager().getPlayerRole(sender.getName());

        if (PluginProxied.getInstance().getGuildsManager().onGuild(sender.getName())){
            PluginProxied.getInstance().getConfigurationManager().getMessages(sender,ConfigurationManager.Message.ALREADY_ON_GUILD).forEach(message -> {
                if (message.contains("%confirm%")) {
                    ChatUtil.sendConfirming(playerRole,message,sender);
                }else{
                    sender.sendMessage(PluginProxied.getInstance().colorize(message.replace("%prefix%", PluginProxied.getInstance().getPrefix())));
                }
            });
            return;
        }








        ConfigurationManager manager = PluginProxied.getInstance().getConfigurationManager();

        String name = arg[1];
        String displayName = arg[2];
        String tag = arg[3].toUpperCase();

        Member master = new Member(RoleType.MASTER.name(), sender.getName(), false);

        PluginProxied.getInstance().getMySQL().createGuild(
                master,
                new Guild(displayName, PluginProxied.getInstance().getConfigurationManager()
                        .getConfigure(sender,ConfigurationManager.Configure.DEFAULT_TAG_COLOR), tag, name, Arrays.asList(master))
        );

        sender.sendMessage(PluginProxied.getInstance().colorize(
                PluginProxied.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.SUCCESSFULLY_CREATED)
                        .replace("%prefix%", PluginProxied.getInstance().getPrefix())
        ));



    }
}
