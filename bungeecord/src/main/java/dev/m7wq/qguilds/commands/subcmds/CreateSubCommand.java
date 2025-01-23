package dev.m7wq.qguilds.commands.subcmds;

import java.util.*;

import dev.m7wq.qguilds.cooldown.Cooldown;
import dev.m7wq.qguilds.cooldown.CooldownManager;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.roles.Role;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.objects.Member;
import dev.m7wq.qguilds.utils.ChatUtil;


public class CreateSubCommand implements SubCommand {






    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {



        ConfigurationManager configurationManager = Plugin.getInstance().getConfigurationManager();

        CooldownManager cd = Plugin.getInstance().getCooldownManager();


        if (cd.ownerCooldown.containsKey(sender.getName()) && cd.isEnabled()) {


            long cooldown = cd.ownerCooldown.get(sender.getName());


            if (cooldown != 0) {

                sender.sendMessage(
                        configurationManager.getMessage(sender, ConfigurationManager.Message.COOLDOWN_MESSAGE)
                                .replace("%prefix%", Plugin.getInstance().getPrefix())
                                .replace("%remaining%", CooldownManager.convertSecondsToTime(
                                        cd.ownerCooldown.get(sender.getName())
                                ))
                );
                return;
            }
        }

        String playerRole = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).getName();

        if (Plugin.getInstance().getGuildsManager().onGuild(sender.getName())){
            Plugin.getInstance().getConfigurationManager().getMessages(sender,ConfigurationManager.Message.ALREADY_ON_GUILD).forEach(message -> {
                if (message.contains("%confirm%")) {
                    ChatUtil.sendConfirming(playerRole,message,sender);
                }else{
                    sender.sendMessage(Plugin.getInstance().colorize(message.replace("%prefix%", Plugin.getInstance().getPrefix())));
                }
            });
            return;
        }


        if (!arg[0].equalsIgnoreCase("create"))
            throw new IllegalStateException("SubCreateGuild issue on executing");




        if (arg.length != 4){
            for (String string : Plugin.getInstance().getConfigurationManager()
                    .getMessages(sender, ConfigurationManager.Message.FAILED_CREATING_GUILD)){
                sender.sendMessage(Plugin.getInstance().colorize(
                        string.replace("%prefix%", Plugin.getInstance().getPrefix())
                ));
            }
            return;
        }

        if (arg[3].length() > 3){
            sender.sendMessage(Plugin.getInstance().colorize(
                    Plugin.getInstance().getConfigurationManager()
                            .getMessage(sender,ConfigurationManager.Message.LONG_TAG)
                            .replace("%prefix%", Plugin.getInstance().getPrefix())
            ));
            return;
        }





        ConfigurationManager manager = Plugin.getInstance().getConfigurationManager();

        String name = arg[1];



        if (ChatUtil.containsSpecialCharacter(name)){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CONTAIN_SPECIAL_CHAR);
            return;
        }

        if (name.length() > 25){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.LONG_NAME);
            return;
        }
        List<Role> defaultRoles = Plugin.getInstance().getRolesManager().getDefaultRoles();
        Guild guild =  new Guild(defaultRoles,
                Plugin.getInstance().getConfigurationManager()
                        .getConfigure(sender,ConfigurationManager.Configure.DEFAULT_TAG_COLOR),
                Plugin.getInstance().getConfigurationManager()
                        .getConfigure(sender,ConfigurationManager.Configure.DEFAULT_TAG)
                , name, new ArrayList<>());

        Member creator = new Member(defaultRoles.get(0), sender.getName(), false);

        guild.getPlayersList().add(creator);
        Plugin.getInstance().getGuildsManager().getGuildMap().put(guild.getGuildName(), guild);



        sender.sendMessage(Plugin.getInstance().colorize(
                Plugin.getInstance().getConfigurationManager().getMessage(sender,ConfigurationManager.Message.SUCCESSFULLY_CREATED)
                        .replace("%prefix%", Plugin.getInstance().getPrefix())
        ));

        if (cd.isEnabled()) {
            cd.ownerCooldown.put(sender.getName(), CooldownManager.daysToSeconds(manager.getConfig().getLong(ConfigurationManager.Configure.CREATE_COOLDOWN.getKey())));
            cd.guildCooldown.put(name, new Cooldown(0, 0, 0));
        }

        BungeeHook.callEvent(new ChangeHappenEvent(null, sender.getName()));
    }
}
