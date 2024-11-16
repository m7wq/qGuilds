package dev.m7wq.qguilds.commands.subcmds;

import dev.m7wq.qguilds.Plugin;
import dev.m7wq.qguilds.enums.RoleType;
import dev.m7wq.qguilds.events.custom.entity.ChangeHappenEvent;
import dev.m7wq.qguilds.interfaces.SubCommand;
import dev.m7wq.qguilds.managers.ConfigurationManager;
import dev.m7wq.qguilds.managers.GuildsManager;
import dev.m7wq.qguilds.utils.ChatUtil;
import dev.m7wq.qguilds.utils.hooks.BungeeHook;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class SetRoleSubCommand implements SubCommand {
    @Override
    public void jobLoad(ProxiedPlayer sender, String... arg) {

        if (!Plugin.getInstance().getGuildsManager().getPlayerRole(sender.getName()).equalsIgnoreCase("MASTER")){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NO_PERMISSION);
            return;
        }

        if (arg.length != 3){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.SET_ROLE_AND_PLAYER));
            return;
        }




        String target = arg[1];

        if (target.equalsIgnoreCase(sender.getName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.CANT_CHANGE_ROLE);
            return;
        }

        GuildsManager guildsManager = Plugin.getInstance().getGuildsManager();

        if(!guildsManager.onGuild(target,guildsManager.getPlayerGuild(sender.getName()).getGuildName())){
            ChatUtil.sendMessage(sender, ConfigurationManager.Message.NOT_IN_YOUR_GUILD);
            return;
        }



        List<String> list = Arrays.asList("MEMBER","MODERATOR","SPECIAL","ADMIN");

        String roleTargeted = arg[2];

        if (!list.contains(roleTargeted)){
            sender.sendMessage(Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.WRONG_ROLE));
            return;
        }

        RoleType roleType = RoleType.getByName(roleTargeted);

        Plugin.getInstance().getMembersManager().setRole(target,roleType);
        sender.sendMessage(
                Plugin.getInstance().getConfigurationManager().getMessage(sender, ConfigurationManager.Message.ROLE_IS_SET)
                        .replace("%target%",target)
                        .replace("%role%",roleType.getDisplayRole())
        );

        BungeeHook.callEvent(new ChangeHappenEvent(null,target));

    }
}
