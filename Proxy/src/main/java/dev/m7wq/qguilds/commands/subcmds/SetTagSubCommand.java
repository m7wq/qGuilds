package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.cooldown.Cooldown;
import dev.m7wq.qguilds.cooldown.CooldownManager;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;


public class SetTagSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {




        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager configurationManager = Plugin.getInstance().getConfigurationManager();

        CooldownManager cd = Plugin.getInstance().getCooldownManager();
        Cooldown cooldown = Plugin.getInstance().getCooldownManager().guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName());


        if (cooldown.getSetTagCooldown() != 0){
            sender.sendMessage(
                    configurationManager.getMessage(sender, ConfigurationManager.Message.COOLDOWN_MESSAGE)
                            .replace("%prefix%",Plugin.getInstance().getPrefix())
                            .replace("%remaining%", CooldownManager.convertSecondsToTime(
                                    cooldown.getSetTagCooldown()
                            ))
            );
            return;
        }

        HashSet<String> canSetTag = new HashSet<>( configurationManager.getConfigures(ConfigurationManager.Configure.CAN_SET_TAG));

        if (!canSetTag.contains(guildsManager.getPlayerRole(sender.getName()))){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }

        if (arg.length != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.ENTER_THE_TAG);
            return;
        }

        String tag = arg[1];

        if (ChatUtil.containsSpecialCharacter(tag)){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CONTAIN_SPECIAL_CHAR);
            return;
        }

        if (tag.length() != 3){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.LONG_OR_SHORT_TAG);
            return;
        }

        Plugin.getInstance().getMySQL().changeTag(tag,guildsManager.getPlayerGuild(sender.getName()).getGuildName());

        sender.sendMessage(
                Plugin.getInstance().colorize( configurationManager.getMessage(sender, ConfigurationManager.Message.CHANGED_TAG)
                        .replace("%new_tag%",tag))
        );

        cd.guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName()).setSetTagCooldown(CooldownManager.daysToSeconds(configurationManager.getConfig().getLong(ConfigurationManager.Configure.TAG_COOLDOWN.getKey())));
        BungeeHook.callEvent(new ChangeHappenEvent(sender,null));

    }
}
