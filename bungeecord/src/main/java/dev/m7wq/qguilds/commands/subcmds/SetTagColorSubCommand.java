package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.cooldown.Cooldown;
import dev.m7wq.qguilds.cooldown.CooldownManager;
import dev.m7wq.qguilds.enums.Permission;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.RolesUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SetTagColorSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();
        ConfigurationManager configurationManager = Plugin.getInstance().getConfigurationManager();

        CooldownManager cd = Plugin.getInstance().getCooldownManager();
        Cooldown cooldown = Plugin.getInstance().getCooldownManager().guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName());


        if (cooldown.getSetTagColorCooldown() != 0 && cd.isEnabled()){
            sender.sendMessage(
                    configurationManager.getMessage(sender, ConfigurationManager.Message.COOLDOWN_MESSAGE)
                            .replace("%prefix%",Plugin.getInstance().getPrefix())
                            .replace("%remaining%", CooldownManager.convertSecondsToTime(
                                    cooldown.getSetTagColorCooldown()
                            ))
            );
            return;
        }

        List<String> canSetTagColor = RolesUtil.getPermissionRoles(sender.getName(), Permission.can_setTagColor);

        if (!canSetTagColor.contains(guildsManager.getPlayerRole(sender.getName()).getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }

        if (arg.length != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.ENTER_TAG_COLOR);
            return;
        }

        String tagColor = arg[1];

        List<Character> minecraftColorCodes = new ArrayList<>(Arrays.asList(
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        ));



        if (tagColor.length() != 2){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.LONG_OR_SHORT_TAG_COLOR);
            return;
        }

        if (!tagColor.startsWith("&")||!tagColor.startsWith("§")){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.COLOR_EXAMPLE);
            return;
        }

        if (!minecraftColorCodes.contains(tagColor.toCharArray()[1])){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_COLOR_CODE);
            return;
        }


        guildsManager.getPlayerGuild(sender.getName()).setTagColor(tagColor);

        sender.sendMessage(
                Plugin.getInstance().colorize( configurationManager.getMessage(sender, ConfigurationManager.Message.CHANGED_TAG_COLOR)
                        .replace("%new_tag_color%",tagColor))
        );
        if (cd.isEnabled()) {
            Plugin.getInstance().getCooldownManager()
                    .guildCooldown.get(guildsManager.getPlayerGuild(sender.getName()).getGuildName()).setSetTagColorCooldown(CooldownManager.hoursToSeconds(configurationManager.getConfig().getLong(ConfigurationManager.Configure.TAG_COLOR_COOLDOWN.getKey())));
        }
        BungeeHook.callEvent(new ChangeHappenEvent(sender,null));

    }

}
