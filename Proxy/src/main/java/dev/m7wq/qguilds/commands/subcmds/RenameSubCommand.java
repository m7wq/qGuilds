package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.cooldown.Cooldown;
import dev.m7wq.qguilds.cooldown.CooldownManager;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.objects.Guild;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;

public class RenameSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {



        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager configurationManager = Plugin.getInstance().getConfigurationManager();

        CooldownManager cd = Plugin.getInstance().getCooldownManager();
        Cooldown cooldown = Plugin.getInstance().getCooldownManager().guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName());


        if (cooldown.getRenameCooldown() != 0){
            sender.sendMessage(
                    configurationManager.getMessage(sender, ConfigurationManager.Message.COOLDOWN_MESSAGE)
                            .replace("%prefix%",Plugin.getInstance().getPrefix())
                            .replace("%remaining%", CooldownManager.convertSecondsToTime(
                                    cooldown.getRenameCooldown()
                            ))
            );
            return;
        }



        HashSet<String> canRename = new HashSet<>(
                configurationManager.getConfigures(ConfigurationManager.Configure.CAN_RENAME)
        );

        if (!canRename.contains(guildsManager.getPlayerRole(sender.getName()))){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }


        if (arg.length != 3){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_RENAME);
            return;
        }

        Guild senderGuild = guildsManager.getPlayerGuild(sender.getName());

        String newName = arg[1];
        String newDisplayName = arg[2];

        if (ChatUtil.containsSpecialCharacter(newDisplayName+newName)){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CONTAIN_SPECIAL_CHAR);
            return;
        }

        if (newName.length() > 25 || newDisplayName.length() > 25){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.LONG_NAME);
            return;
        }

        String successfullyRenamedMessage = configurationManager.getMessage(sender, ConfigurationManager.Message.SUCCESSFULLY_RENAMED)
                .replace("%old_name%",senderGuild.getGuildName())
                .replace("%new_name%",newName);

        Plugin.getInstance().getMySQL().renameTheGuild(senderGuild,newName,newDisplayName);

        sender.sendMessage(Plugin.getInstance().colorize(successfullyRenamedMessage));

        cd.guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName()).setRenameCooldown(CooldownManager.daysToSeconds(configurationManager.getConfig().getLong(ConfigurationManager.Configure.RENAME_COOLDOWN.getKey())));


        BungeeHook.callEvent(new ChangeHappenEvent(sender,null));
    }
}
