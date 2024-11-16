package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import sun.util.resources.cldr.wae.CalendarData_wae_CH;

import java.util.HashSet;
import java.util.List;

public class MyPermissionsSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        List<String> message = Plugin.getInstance().getConfigurationManager().getMessages(sender, ConfigurationManager.Message.PERMISSIONS_MESSAGE);


        ConfigurationManager cfg = Plugin.getInstance().getConfigurationManager();

        String role = Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName());

        List<String> canInvite,canMute,canUnMute,canKick,canPromote,canDemote,canSetTag,canSetTagColor,canRename;

        canInvite = cfg.getConfigures(ConfigurationManager.Configure.CAN_INVITE);
        canMute = cfg.getConfigures(ConfigurationManager.Configure.CAN_MUTE);
        canUnMute = cfg.getConfigures(ConfigurationManager.Configure.CAN_UNMUTE);
        canKick = cfg.getConfigures(ConfigurationManager.Configure.CAN_KICK);
        canPromote = cfg.getConfigures(ConfigurationManager.Configure.CAN_PROMOTE);
        canDemote = cfg.getConfigures(ConfigurationManager.Configure.CAN_DEMOTE);
        canSetTag = cfg.getConfigures(ConfigurationManager.Configure.CAN_SET_TAG);
        canSetTagColor = cfg.getConfigures(ConfigurationManager.Configure.CAN_SET_TAG_COLOR);
        canRename = cfg.getConfigures(ConfigurationManager.Configure.CAN_RENAME);

        for (String string : message){
            string = string.replace("%invite%",doneMark(role,canInvite))
                    .replace("%mute%",doneMark(role,canMute))
                    .replace("%unmute%",doneMark(role,canUnMute))
                    .replace("%kick%",doneMark(role,canKick))
                    .replace("%promote%",doneMark(role,canPromote))
                    .replace("%demote%",doneMark(role,canDemote))
                    .replace("%set-tag%",doneMark(role,canSetTag))
                    .replace("%set-tag-color%",doneMark(role,canSetTagColor))
                    .replace("%rename%",doneMark(role,canRename))
                            .replace("%prefix%",Plugin.getInstance().getPrefix());

            sender.sendMessage(Plugin.getInstance().colorize(string));
        }

    }

    String doneMark(String role, List<String> allowedRoles){
        return getMark(allowedRoles.contains(role));
    }

    String getMark(boolean bool){
        if (bool){
            return "&a✔";
        }else {
            return "&c✗";
        }
    }
}
